package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper

class EventDetailViewModel: ViewModel() {
    val repository = EventerizeApp.getInstance().repository
    val error = MutableLiveData<EventerizeError>()
    val event = MutableLiveData<EventWrapper>()


    fun getEventById(id:String){
        repository.getEventById(id)
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
                        event.postValue(EventWrapper(it.result))
                    }
                }
            }
    }

    fun getEvent():LiveData<EventWrapper> = event
}