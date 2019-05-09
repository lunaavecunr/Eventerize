package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.EventListAdapter
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.EventListViewModel
import kotlinx.android.synthetic.main.fragment_event_list.*

private const val INTENT_TAB_EXTRA = "INTENT_TAB_EXTRA"

class EventListFragment : BaseFragment<EventListViewModel>() {

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

        val updateEvent = Observer<List<EventWrapper>> {
            updateList(it)
        }

        val updateError = Observer<EventerizeError> {
            showError(context!!, it.message)
        }

        viewModel.getEvent().observe(this,updateEvent)
        viewModel.getError().observe(this,updateError)

        when (arguments?.getString(INTENT_TAB_EXTRA)) {
            "all" -> {
                viewModel.retrievalAllEvent()
            }
            "orga" -> {
                viewModel.retrievalEventByOrga()
            }
            "member" -> {
                viewModel.retrievalEventByMember()
            }
        }
    }

    fun updateList(eventList: List<EventWrapper>) {
        adapter.updateEventList(eventList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_list, container, false)

    }




    companion object {
        fun newInstance(identifier: String = ""): EventListFragment {
            val fragment = EventListFragment()
            val args = Bundle()
            args.putString(INTENT_TAB_EXTRA, identifier)
            fragment.arguments = args
            return fragment
        }
    }
}