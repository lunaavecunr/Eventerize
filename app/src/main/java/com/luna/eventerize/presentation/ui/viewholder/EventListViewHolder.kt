package com.luna.eventerize.presentation.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.parse.ParseUser

class EventListViewHolder(v: View): RecyclerView.ViewHolder(v){

    private val eventTitle: TextView = itemView.findViewById(R.id.adapter_event_list_title)
    private val eventMapText: TextView = itemView.findViewById(R.id.adapter_event_list_map_text)
    private val eventCalendarText: TextView = itemView.findViewById(R.id.adapter_event_list_calendar_text)
    private val eventImage: ImageView = itemView.findViewById(R.id.adapter_event_list_image)
    private val eventCrown: ImageView = itemView.findViewById(R.id.adapter_event_list_crown)


    fun bindSet(eventWrapper: EventWrapper, onEventClick: ((EventWrapper)->Unit)?){

        eventTitle.text = eventWrapper.event.title
        eventMapText.text = eventWrapper.event.location
        eventCalendarText.text = eventWrapper.startDateToFormat("dd/MM/yyyy HH:mm")
        eventCrown.visibility = View.INVISIBLE

        if (eventWrapper.event.logo != null) {
            EventerizeApp.getInstance().picasso.load(eventWrapper.event.logo!!.url).into(eventImage)
        }

        if(eventWrapper.event.owner!!.objectId == ParseUser.getCurrentUser().objectId) {
            eventCrown.visibility = View.VISIBLE
        }

        itemView.setOnClickListener{
            onEventClick?.let {
                it(eventWrapper)
            }
        }

    }

}