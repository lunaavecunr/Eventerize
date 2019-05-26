package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.fragment_event_details.*
import kotlinx.android.synthetic.main.fragment_event_list.*
import kotlinx.android.synthetic.main.fragment_list_members.*

private const val INTENT_LIST_MEMBERS_ID_EXTRA = "INTENT_LIST_MEMBERS_ID_EXTRA"


class MembersListFragment : BaseFragment<MembersListViewModel>() {

    private lateinit var adapter: MembersListAdapter
    private lateinit var navigator: Navigator
    private lateinit var event: Event
    override val viewModelClass = MembersListViewModel::class

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = MembersListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        activity!!.title = getString(R.string.fragment_list_members_title)
        (activity as AppCompatActivity).setSupportActionBar(fragment_list_member_toolbar)
        fragment_list_member_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)

        navigator = Navigator(fragmentManager!!)

        fragment_list_member_recyclerView.layoutManager = LinearLayoutManager(context)

        fragment_list_member_recyclerView.adapter = adapter

        adapter.setOnUserClick { onUserClick(it) }

        val updateEvent = Observer<Event> {
            this.event = it
            val list: MutableList<ParseUser> = mutableListOf()
            if(it.members != null) {
                list.addAll(it.members!!)
                updateList(list, it.owner!!)
            }
        }

        viewModel.getEventsRetrivial().observe(this, updateEvent)

        viewModel.getEventById(arguments?.getString(INTENT_LIST_MEMBERS_ID_EXTRA)!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateList(userList: List<ParseUser>, owner: ParseUser) {
        adapter.updateUserList(userList, owner)
    }

    private fun onUserClick(user: ParseUser){
        val list: MutableList<ParseUser> = mutableListOf()
        list.addAll(this.event.members!!)
        list.remove(user)
        this.event.members = list
        viewModel.removeUser(this.event)
        updateList(list, this.event.owner!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_members, container, false)

    }

    companion object {
        fun newInstance(identifier: String = ""): MembersListFragment {
            val fragment = MembersListFragment()
            val args = Bundle()
            args.putString(INTENT_LIST_MEMBERS_ID_EXTRA, identifier)
            fragment.arguments = args
            return fragment
        }
    }
}