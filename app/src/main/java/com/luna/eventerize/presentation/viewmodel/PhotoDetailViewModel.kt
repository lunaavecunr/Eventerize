package com.luna.eventerize.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import bolts.Task
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.repository.EventerizeRepo

class PhotoDetailViewModel: ViewModel(){
    private var repository = EventerizeRepo()

    fun destroyPicture(eventId:String, photoIdInt: Int){
        val eventList: Task<Event> = repository.getEventById(eventId)
        eventList.result.images!!.toMutableList().removeAt(photoIdInt)
        Log.d("mlk", eventList.result.images!!.size.toString())
        Log.d("mlk", eventList.result.images!!.size.toString())
        //repository.saveEvent(eventList.result)
    }
}