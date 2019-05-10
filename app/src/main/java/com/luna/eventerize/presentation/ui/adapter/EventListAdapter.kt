package com.luna.eventerize.presentation.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.luna.eventerize.presentation.ui.viewholder.EventListViewHolder
import com.luna.eventerize.presentation.utils.inflate
import com.parse.ParseUser

class EventListAdapter: RecyclerView.Adapter<EventListViewHolder>(){

    private val eventList: MutableList<EventWrapper> = mutableListOf()
    private  var onEventClickListener: ((EventWrapper)->Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val inflatedView = parent.inflate(R.layout.adapter_event_list, false)
        return EventListViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val userSet = eventList[position]
        holder.bindSet(userSet,onEventClickListener)
    }

    fun addEventList(eventList : List<EventWrapper>){
        this.eventList.addAll(eventList)
        notifyDataSetChanged()
    }

    fun updateEventList(eventList: List<EventWrapper>){
        this.eventList.clear()
        addEventList(eventList)
    }

    fun setOnEventClick(onEventClick: (EventWrapper)->Unit){
        onEventClickListener = onEventClick
    }

}