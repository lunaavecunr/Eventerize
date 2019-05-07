package com.luna.eventerize.presentation.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.ui.viewholder.EventListViewHolder
import com.luna.eventerize.presentation.utils.inflate

class EventListAdapter: RecyclerView.Adapter<EventListViewHolder>() {

    private val eventList: MutableList<Event> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val inflatedView = parent.inflate(R.layout.adapter_event_list, false)
        return EventListViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val userSet = eventList[position]

        holder.bindSet(userSet)
    }

    fun addEventList(eventList : List<Event>){
        this.eventList.addAll(eventList)
        notifyDataSetChanged()
    }

    fun updateEventList(eventList: List<Event>){
        this.eventList.clear()
        addEventList(eventList)
    }

}