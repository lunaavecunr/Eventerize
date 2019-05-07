package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.MembersListAdapter
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.MembersListViewModel
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_event_list.*

class MembersListFragment : BaseFragment<MembersListViewModel>() {

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

        activity!!.title = getString(R.string.fragment_list_members_title)
        (activity as AppCompatActivity).setSupportActionBar(fragment_event_list_toolbar)

        navigator = Navigator(fragmentManager!!)

        fragment_event_list_recycler_view.layoutManager = LinearLayoutManager(context)

        fragment_event_list_recycler_view.adapter = adapter

        val updateEvent = Observer<Event> {
            var list: MutableList<ParseUser> = mutableListOf()
            list.addAll(it.members!!)
            updateList(list)
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

    fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): MembersListFragment =  MembersListFragment()
    }
}