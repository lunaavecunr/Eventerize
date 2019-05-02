package com.luna.eventerize.presentation.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event

class EventListViewHolder(var v: View): RecyclerView.ViewHolder(v) {

    private val eventTitle: TextView = itemView.findViewById(R.id.adapter_event_list_title)
    private val eventMapText: TextView = itemView.findViewById(R.id.adapter_event_list_map_text)
    private val eventCalendarText: TextView = itemView.findViewById(R.id.adapter_event_list_calendar_text)
    private val eventImage: ImageView = itemView.findViewById(R.id.adapter_event_list_image)

    fun bindSet(event: Event){

        eventTitle.text = event.title
        eventMapText.text = event.place
        eventCalendarText.text = event.date
        eventImage.setImageResource(event.image)

    }

}