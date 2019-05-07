package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.EventListAdapter
import com.luna.eventerize.presentation.ui.adapter.MembersListAdapter
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.EventListViewModel
import com.luna.eventerize.presentation.viewmodel.MembersListViewModel
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_event_list.*

class MembersListFragment : BaseFragment<MembersListViewModel>(), View.OnClickListener {

    private lateinit var adapter: MembersListAdapter
    private lateinit var navigator: Navigator
    override val viewModelClass = MembersListViewModel::class

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = MembersListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        navigator = Navigator(fragmentManager!!)

        fragment_event_list_recycler_view.layoutManager = LinearLayoutManager(context)

        fragment_event_list_recycler_view.adapter = adapter

        fragment_event_list_fab.setOnClickListener(this)

        val updateEvent = Observer<Event> {
            updateList(it.owner)
        }

        viewModel.getEventsRetrivial().observe(this, updateEvent)

        viewModel.getEventById("2nM4kiATyT")
    }

    fun updateList(userList: List<ParseUser>) {
        adapter.updateUserList(userList)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_list, container, false)

    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.fragment_event_list_fab -> {
                navigator.createList()
                activity?.title = "Création de liste"

            }
        }

    }

    fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): EventListFragment =  EventListFragment()
    }
}