package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.data.model.Event

class EventListViewModel: ViewModel() {

    var repo = EventerizeApp.getInstance().repository
    var listEvents = MutableLiveData<List<Event>>()

    fun getEvents(){

    }

    fun getListEvents(): LiveData<List<Event>> = listEvents

}