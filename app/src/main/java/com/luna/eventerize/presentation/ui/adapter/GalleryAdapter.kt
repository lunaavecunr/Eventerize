package com.luna.eventerize.presentation.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.luna.eventerize.presentation.ui.datawrapper.ImageWrapper
import com.luna.eventerize.presentation.ui.viewholder.GalleryViewHolder
import com.luna.eventerize.presentation.utils.inflate

class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {
    private val imageList: MutableList<ImageWrapper> = mutableListOf()
    private  var onImageClickListener: ((ImageWrapper)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflatedView = parent.inflate(R.layout.adapter_gallery, false)
        return GalleryViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val userSet = imageList[position]
        holder.bindSet(userSet,onImageClickListener)
    }

    fun addImageList(imageList : List<ImageWrapper>){
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    fun updateImageList(imageList: List<ImageWrapper>){
        this.imageList.clear()
        addImageList(imageList)
    }

    fun setOnImageClick(onEventClick: (ImageWrapper)->Unit){
        onImageClickListener = onEventClick
    }
}