package com.luna.eventerize.presentation.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.data.model.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_event_ressource_card_view.view.*

class EventDetailViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val photo = view.event_details_ressource_photo
    val layout = view.event_details_card_view
    val isSelected = view.user_details_photo_is_checked
    val isUserPicture = view.user_details_photo_is_user_picture

    var isOwnPicture = false

    fun bindSet(image: Image){
        EventerizeApp.getInstance().picasso.load(image.file!!.url).into(photo)
        if(!isOwnPicture){
            isUserPicture.visibility = View.INVISIBLE
        }
        isSelected.visibility = View.INVISIBLE
        selectImage()
    }

    private fun selectImage(){
        layout.setOnClickListener {
            if(isSelected.visibility == View.INVISIBLE){
                isSelected.visibility = View.VISIBLE
            }else{
                isSelected.visibility = View.INVISIBLE
            }
        }
    }
}
