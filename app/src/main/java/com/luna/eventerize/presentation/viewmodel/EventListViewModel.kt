package com.luna.eventerize.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.data.model.Image
import com.parse.ParseUser

class EventListViewModel: ViewModel() {

    var repository = EventerizeApp.getInstance().repository
    var error = MutableLiveData<EventerizeError>()
    var eventsRetrivial = MutableLiveData<ArrayList<Event>>()
    var eventsImageRetrivial = MutableLiveData<ArrayList<Event>>()
    var eventsMembersRetrivial = MutableLiveData<ArrayList<Event>>()
    var eventsOwnersRetrivial = MutableLiveData<ArrayList<Event>>()
    var eventsSortedByOwner = MutableLiveData<ArrayList<Event>>()
    var eventsSortedByMembers = MutableLiveData<ArrayList<Event>>()


    var events =  ArrayList<Event>()

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

    fun getOwnersEvent() {
        for (event in events){
            repository.getRelation(event.parse.owner).continueWith {
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
                        val owners = ArrayList<ParseUser>()
                        for (user in it.result) {
                            owners.add(user)
                        }
                        event.owner = owners

                        eventsOwnersRetrivial.postValue(events)
                    }
                }
            }
        }
    }

    fun sortCurrentUserByOwner() {
        val eventByOwner = ArrayList<Event>()
        events.map {value ->
            if(value.owner != null){
                for (user in value.owner!!) {
                    if(user.objectId == ParseUser.getCurrentUser().objectId) {
                        eventByOwner.add(value)
                    }
                }

            }

        }

        eventsSortedByOwner.postValue(eventByOwner)
    }

    fun sortCurrentUserByMembers() {
        val eventByMembers = ArrayList<Event>()
        events.map {value ->
            if(value.members != null){
                for (user in value.members!!) {
                    if(user.objectId == ParseUser.getCurrentUser().objectId) {
                        eventByMembers.add(value)
                    }
                }

            }

        }

        eventsSortedByMembers.postValue(eventByMembers)
    }

    fun getEventsRetrivial() : LiveData<ArrayList<Event>> = eventsRetrivial
    fun getEventsImageRetrivial() : LiveData<ArrayList<Event>> = eventsImageRetrivial
    fun getEventsMembersRetrivial() : LiveData<ArrayList<Event>> = eventsMembersRetrivial
    fun getEventsOwnersRetrivial() : LiveData<ArrayList<Event>> = eventsOwnersRetrivial
    fun getEventsSortByOwner() : LiveData<ArrayList<Event>> = eventsSortedByOwner
    fun getEventsSortByMembers() : LiveData<ArrayList<Event>> = eventsSortedByMembers
}