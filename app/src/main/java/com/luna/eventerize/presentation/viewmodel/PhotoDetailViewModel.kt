package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.data.repository.EventerizeRepo
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper

class PhotoDetailViewModel: ViewModel(){
    private var repository = EventerizeRepo()
    private var successGettingImage = MutableLiveData<Boolean>()
    private var successDeletingOperation = MutableLiveData<Boolean>()
    private var numericalPhotoId:Int = 0
    var error = MutableLiveData<EventerizeError>()


    fun selectPicture(eventId:String, photoId: String){
        repository.getEventById(eventId).continueWith { singleEvent ->
            when {
                singleEvent.isCancelled -> {
                    error.postValue(
                        EventerizeError(
                            EventerizeApp.getInstance().getString(R.string.cancelled_searching_image_to_destroy),
                            EventerizeApp.getInstance().getString(R.string.error)
                        )
                    )
                }
                singleEvent.isFaulted -> {
                    error.postValue(
                        EventerizeError(
                            EventerizeApp.getInstance().getString(R.string.error_while_searching_image_to_destroy),
                            EventerizeApp.getInstance().getString(R.string.error)
                        )
                    )
                }
                else->{
                    singleEvent.result.images!!.mapIndexed { index, singlePhoto ->
                        if (singlePhoto.objectId == photoId) {
                            numericalPhotoId = index
                            successGettingImage.postValue(true)
                        }
                    }
                    singleEvent.result
                }
            }
        }
    }

    fun destroyPicture(eventId:String, photoId: String){
        repository.getEventById(eventId).continueWith { event ->
            val list = event.result.images!!.toMutableList()
            list.removeAt(numericalPhotoId)
            event.result.images = list
            repository.saveEvent(event.result).continueWith { returnDelete ->
                if (returnDelete.isFaulted) {
                    error.postValue(
                        EventerizeError(
                            EventerizeApp.getInstance().getString(R.string.error_while_destroying_image),
                            EventerizeApp.getInstance().getString(R.string.error)
                        )
                    )
                } else if (returnDelete.isCancelled) {
                    error.postValue(
                        EventerizeError(
                            EventerizeApp.getInstance().getString(R.string.error_while_destroying_image),
                            EventerizeApp.getInstance().getString(R.string.error)
                        )
                    )
                } else {
                    repository.destroyImage(photoId)
                }
            }
        }
    }

    var getSuccess:MutableLiveData<Boolean> = successGettingImage
}