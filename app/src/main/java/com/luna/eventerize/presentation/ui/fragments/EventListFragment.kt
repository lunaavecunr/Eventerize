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
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.EventListViewModel
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_event_list.*

class EventListFragment : BaseFragment<EventListViewModel>(), View.OnClickListener {

    private lateinit var adapter: EventListAdapter
    private lateinit var navigator: Navigator
    override val viewModelClass = EventListViewModel::class

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = EventListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        navigator = Navigator(fragmentManager!!)

        fragment_event_list_recycler_view.layoutManager = LinearLayoutManager(context)

        fragment_event_list_recycler_view.adapter = adapter

        fragment_event_list_fab.setOnClickListener(this)

        val updateEvent = Observer<ArrayList<Event>> {
            viewModel.getOwnersEvent()
            viewModel.getMembersEvent()
        }

        val updateEventOwners = Observer<ArrayList<Event>> {
            viewModel.sortCurrentUserByOwner()
        }

        val updateEventMembers = Observer<ArrayList<Event>> {
            viewModel.sortCurrentUserByMembers()
        }

        val updateEventSortByOwner = Observer<ArrayList<Event>> {
            updateList(it)
        }

        val updateEventSortByMembers = Observer<ArrayList<Event>> {
            updateList(it)
        }
/*

        val updateEventImage = Observer<ArrayList<Event>> {
            for (event in it) {
                for (image in event.images!!) {
                    Log.d("mlk", "URL: "+image.file!!.url)
                    Log.d("mlk", "ID user: "+image.user!!.objectId)
                    Log.d("mlk", "string: "+image.string)
                }
            }
        }

        val updateEventMembers = Observer<ArrayList<Event>> {
            for (event in it) {
                for(user in event.members!!){
                    Log.d("mlk", "id User: "+user.objectId)
                    Log.d("mlk", "username: "+user.username)
                }
            }
            viewModel.getImageEvent()
        }
*/

//        viewModel.getEventsMembersRetrivial().observe(this,updateEventMembers)
//        viewModel.getEventsImageRetrivial().observe(this,updateEventImage)
        viewModel.getEventsSortByOwner().observe(this, updateEventSortByOwner)
        viewModel.getEventsSortByMembers().observe(this, updateEventSortByMembers)
        viewModel.getEventsOwnersRetrivial().observe(this, updateEventOwners)
        viewModel.getEventsMembersRetrivial().observe(this,updateEventMembers)
        viewModel.getEventsRetrivial().observe(this, updateEvent)

        viewModel.getEvent()
    }

    fun updateList(eventList: List<Event>) {
        adapter.updateEventList(eventList)
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