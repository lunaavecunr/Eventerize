package com.luna.eventerize.presentation.viewmodel.createevent

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.EventerizeError
import com.parse.ParseFile
import java.io.ByteArrayOutputStream
import java.util.*


class CreateEventViewModel : ViewModel() {
    var repository = EventerizeApp.getInstance().repository
    var error = MutableLiveData<EventerizeError>()
    var successUpload = MutableLiveData<Boolean>()

    fun saveEvent(
        title: String,
        location: String,
        startDate: Date?,
        startHour: Date?,
        endDate: Date?,
        endHour: Date?,
        logo: Bitmap?
    ) {

        if (title.isBlank() || location.isBlank() || startDate == null || startHour == null || endDate == null || endHour == null) {
            error.postValue(
                EventerizeError(
                    "Un champ n'a pas été rempli!",
                    "Erreur lors de la création de l'évènement"
                )
            )
            return
        }

        val event = Event()
        event.title = title
        event.location = location
        event.startDate = formatDate(startDate, startHour)
        event.endDate = formatDate(endDate, endHour)
        if (logo != null) {
            val stream = ByteArrayOutputStream()
            logo.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val image = ParseFile(byteArray)
            event.logo = image
        } else {
            val logo = ContextCompat.getDrawable(EventerizeApp.getInstance(), R.mipmap.eventerize)
            val stream = ByteArrayOutputStream()
            logo!!.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val image = ParseFile(byteArray)
            event.logo = image
        }
        repository.saveEvent(event)
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
                        successUpload.postValue(true)
                    }
                }
            }
    }

    fun formatDate(date: Date, hour: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hourCalendar = Calendar.getInstance()
        hourCalendar.time = hour
        calendar.set(Calendar.HOUR_OF_DAY, hourCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, hourCalendar.get(Calendar.MINUTE))
        return Date(calendar.timeInMillis)
    }

    fun getSuccess(): LiveData<Boolean> = successUpload

    fun getError(): LiveData<EventerizeError> = error
}