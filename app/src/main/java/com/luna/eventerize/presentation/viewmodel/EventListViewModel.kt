package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper

class EventListViewModel: ViewModel() {

    var repository = EventerizeApp.getInstance().repository
    var error = MutableLiveData<EventerizeError>()
    var events = MutableLiveData<List<EventWrapper>>()

    fun retrievalAllEvent () {
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
                        val listEventWrapper = ArrayList<EventWrapper>()
                        it.result.map { event ->
                            listEventWrapper.add(EventWrapper(event))
                        }
                        events.postValue(listEventWrapper)
                    }
                }
            }
    }
    fun retrievalEventByOrga () {
        repository.getEventByOwner()
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
                        val listEventWrapper = ArrayList<EventWrapper>()
                        it.result.map { event ->
                            listEventWrapper.add(EventWrapper(event))
                        }
                        events.postValue(listEventWrapper)
                    }
                }
            }
    }
    fun retrievalEventByMember () {
        repository.getEventByMembers()
        repository.getEventByOwner()
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
                        val listEventWrapper = ArrayList<EventWrapper>()
                        it.result.map { event ->
                            listEventWrapper.add(EventWrapper(event))
                        }
                        events.postValue(listEventWrapper)
                    }
                }
            }
    }

    fun getEvent(): LiveData<List<EventWrapper>> = events
    fun getError(): LiveData<EventerizeError> = error
}