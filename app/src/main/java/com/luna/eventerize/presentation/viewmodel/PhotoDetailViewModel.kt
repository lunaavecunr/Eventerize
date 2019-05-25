package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.data.repository.EventerizeRepo
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper

class PhotoDetailViewModel : ViewModel() {
    private var repository = EventerizeRepo()
    private var successDeletingOperation = MutableLiveData<Boolean>()
    var error = MutableLiveData<EventerizeError>()


    fun destroyPicture(eventId: String, photoId: String) {
        repository.getEventById(eventId).continueWith { response ->
            when {
                response.isCancelled -> {
                    error.postValue(
                        EventerizeError(
                            EventerizeApp.getInstance().getString(R.string.error_while_destroying_image),
                            EventerizeApp.getInstance().getString(R.string.destroying_image_error)
                        )
                    )
                }
                response.isFaulted -> {
                    error.postValue(
                        EventerizeError(
                            response.error.message.toString(),
                            EventerizeApp.getInstance().getString(R.string.destroying_image_error)
                        )
                    )
                }
                else -> {
                    var event = response.result
                    val list = event.images!!.toMutableList()
                    val index = list.indexOfFirst { it.objectId == photoId }
                    if (index != -1) {
                        list.removeAt(index)
                    }
                    event.images = list
                    repository.saveEvent(event).continueWith {
                        when {
                            it.isCancelled -> {
                                error.postValue(
                                    EventerizeError(
                                        EventerizeApp.getInstance().getString(R.string.error_while_destroying_image),
                                        EventerizeApp.getInstance().getString(R.string.destroying_image_error)
                                    )
                                )
                            }
                            it.isFaulted -> {
                                error.postValue(
                                    EventerizeError(
                                        it.error.message.toString(),
                                        EventerizeApp.getInstance().getString(R.string.destroying_image_error)
                                    )
                                )
                            }
                            else -> {
                                successDeletingOperation.postValue(true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getSuccessDeletingOperation(): LiveData<Boolean> = successDeletingOperation

    fun getError(): LiveData<EventerizeError> = error
}