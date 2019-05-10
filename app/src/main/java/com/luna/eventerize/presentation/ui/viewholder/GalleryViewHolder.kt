package com.luna.eventerize.presentation.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.datawrapper.ImageWrapper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_gallery.view.*

class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val image = view.adapter_gallery_image
    fun bindSet(imageWrapper:ImageWrapper, onImageClick: ((ImageWrapper)->Unit)?){
        Picasso.get().load(imageWrapper.image.file!!.url).into(image)
        itemView.setOnClickListener{
            onImageClick?.let {
                it(imageWrapper)
            }
        }
    }
}