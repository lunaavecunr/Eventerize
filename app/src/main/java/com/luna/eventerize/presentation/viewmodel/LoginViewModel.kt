package com.luna.eventerize.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.*
import com.parse.ParseUser



class LoginViewModel: ViewModel() {
    var repository = EventerizeApp.getInstance().repository
    var error = MutableLiveData<EventerizeError>()
    var user = MutableLiveData<ParseUser>()
    var eventsRetrivial = MutableLiveData<ArrayList<Event>>()
    var eventsImageRetrivial = MutableLiveData<ArrayList<Event>>()
    var eventsMembersRetrivial = MutableLiveData<ArrayList<Event>>()


    var events =  ArrayList<Event>()

    fun login(username : String, password : String) {
        if(username.isBlank() || password.isBlank()) {
            error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.error_field_empty), EventerizeApp.getInstance().getString(R.string.login_error_title)))
            return
        }
        repository.login(username, password)
            .continueWith {
                when {
                    it.isCancelled -> {
                        error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.login_connection_failed), EventerizeApp.getInstance().getString(R.string.login_error_title)))
                    }
                    it.isFaulted -> {
                        error.postValue(EventerizeError(it.error.message.toString(), EventerizeApp.getInstance().getString(R.string.login_error_title)))
                    }
                    else -> {
                        user.postValue(it.result)
                    }
                }
            }
    }

    fun getEvent() {
        events.clear()
        repository.getEvent()
            .continueWith {
            when {
                it.isCancelled -> {
                    error.postValue(
                        EventerizeError(
                            EventerizeApp.getInstance().getString(R.string.login_connection_failed),
                            EventerizeApp.getInstance().getString(R.string.login_error_title)
                        )
                    )
                }
                it.isFaulted -> {
                    error.postValue(
                        EventerizeError(
                            it.error.message.toString(),
                            EventerizeApp.getInstance().getString(R.string.login_error_title)
                        )
                    )
                }
                else -> {
                    for (eventParse in it.result) {
                        events.add(Event(eventParse))
                    }
                    eventsRetrivial.postValue(events)
                }
            }
        }
    }

    fun getImageEvent() {
        for (event in events){
            repository.getRelation(event.parse.images).continueWith {
                when {
                    it.isCancelled -> {
                        error.postValue(
                            EventerizeError(
                                EventerizeApp.getInstance().getString(R.string.login_connection_failed),
                                EventerizeApp.getInstance().getString(R.string.login_error_title)
                            )
                        )
                        Log.d("mlk", "err")
                    }
                    it.isFaulted -> {
                        error.postValue(
                            EventerizeError(
                                it.error.message.toString(),
                                EventerizeApp.getInstance().getString(R.string.login_error_title)
                            )
                        )
                        Log.d("mlk", it.error.message.toString())
                    }
                    else -> {
                        var images = ArrayList<Image>()
                        for (imageParse in it.result) {
                            images.add(Image(imageParse))
                        }
                        event.images = images
                        eventsImageRetrivial.postValue(events)
                    }
                }
            }
        }
    }

    fun getMembersEvent() {
        for (event in events){
            repository.getRelation(event.parse.members).continueWith {
                when {
                    it.isCancelled -> {
                        error.postValue(
                            EventerizeError(
                                EventerizeApp.getInstance().getString(R.string.login_connection_failed),
                                EventerizeApp.getInstance().getString(R.string.login_error_title)
                            )
                        )
                        Log.d("mlk", "err")
                    }
                    it.isFaulted -> {
                        error.postValue(
                            EventerizeError(
                                it.error.message.toString(),
                                EventerizeApp.getInstance().getString(R.string.login_error_title)
                            )
                        )
                        Log.d("mlk", it.error.message.toString())
                    }
                    else -> {
                        var members = ArrayList<ParseUser>()
                        for (user in it.result) {
                            members.add(user)
                        }
                        event.members = members
                        eventsMembersRetrivial.postValue(events)
                    }
                }
            }
        }
    }




    fun getError() : LiveData<EventerizeError> = error
    fun getUser() : LiveData<ParseUser> = user

    fun getEventsRetrivial() : LiveData<ArrayList<Event>> = eventsRetrivial
    fun getEventsImageRetrivial() : LiveData<ArrayList<Event>> = eventsImageRetrivial
    fun getEventsMembersRetrivial() : LiveData<ArrayList<Event>> = eventsMembersRetrivial
}

