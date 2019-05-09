package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.EventerizeError

class EventListViewModel: ViewModel() {

    var repository = EventerizeApp.getInstance().repository
    var error = MutableLiveData<EventerizeError>()
    var events = MutableLiveData<List<Event>>()

    fun retrievalAllEvent () {
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
                        events.postValue(it.result)
                    }
                }
            }
    }

    fun getEvent(): LiveData<List<Event>> = events
    fun getError(): LiveData<EventerizeError> = error
}