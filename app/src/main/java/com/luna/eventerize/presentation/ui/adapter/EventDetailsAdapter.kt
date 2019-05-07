package com.luna.eventerize.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_event_ressource_card_view.view.*

class EventDetailsAdapter(val galleryList:ArrayList<String>) : RecyclerView.Adapter<EventDetailsRessourceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsRessourceViewHolder {
        return EventDetailsRessourceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.details_event_ressource_card_view,parent,false))
    }

    override fun getItemCount() = galleryList.size

    override fun onBindViewHolder(holder: EventDetailsRessourceViewHolder, position: Int) {
        Picasso.get().load(galleryList[position]).resize(100,100).into(holder.photo)
    }
}

class EventDetailsRessourceViewHolder(view:View) : RecyclerView.ViewHolder(view){
    val photo = view.event_details_ressource_photo
}