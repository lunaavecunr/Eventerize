package com.luna.eventerize.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.ui.viewholder.EventDetailViewHolder
import com.squareup.picasso.Picasso

class EventDetailsAdapter(val event:Event) : RecyclerView.Adapter<EventDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailViewHolder {
        return EventDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.details_event_ressource_card_view,parent,false))
    }

    override fun getItemCount() = event.images!!.size

    override fun onBindViewHolder(holder: EventDetailViewHolder, position: Int) {
        holder.bindSet(event.images!![position])
        Picasso.get().load(event.images!![position].file!!.url).resize(100,100).into(holder.photo)
    }
}