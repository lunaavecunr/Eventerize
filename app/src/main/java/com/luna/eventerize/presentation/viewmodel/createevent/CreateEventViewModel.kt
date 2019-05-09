package com.luna.eventerize.presentation.viewmodel.createevent

import android.graphics.Bitmap
import android.util.Log
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
            event.logo = ParseFile(generateByteArray(logo))
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

    private fun formatDate(date: Date, hour: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val hourCalendar = Calendar.getInstance()
        hourCalendar.time = hour
        calendar.set(Calendar.HOUR_OF_DAY, hourCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, hourCalendar.get(Calendar.MINUTE))
        return Date(calendar.timeInMillis)
    }


    private fun generateByteArray(bitmap: Bitmap):ByteArray?{
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        return bos.toByteArray()
    }

    fun getSuccess(): LiveData<Boolean> = successUpload

    fun getError(): LiveData<EventerizeError> = error
}