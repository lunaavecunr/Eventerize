package com.luna.eventerize.presentation.ui.datawrapper

import com.google.android.material.textfield.TextInputEditText
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import java.text.SimpleDateFormat
import java.util.*

class EventWrapper (
    val event: Event
) {
    fun startDateToFormat(formatDate: String): String {
        val simpleFormat = SimpleDateFormat(formatDate, Locale.FRENCH)
        return simpleFormat.format(event.startDate)
    }

    fun endDateToFormat(formatDate: String): String {
        val simpleFormat = SimpleDateFormat(formatDate, Locale.FRENCH)
        return simpleFormat.format(event.endDate)
    }

    fun numberOfMembers():String{
        if(event.members != null) {
            return "${event.members!!.size} ${if (event.members!!.size < 2) {
                EventerizeApp.getInstance().getString(R.string.participant_label_single)
            } else {
                EventerizeApp.getInstance().getString(
                    R.string.participant_label_plural
                )
            }}"
        }else{
            return EventerizeApp.getInstance().getString(R.string.no_members)
        }
    }

    fun dateCoverLabel():String{
        return "${EventerizeApp.getInstance().getString(R.string.begin_label)} ${startDateToFormat("dd/MM/yyyy à HH:mm")}\n" +
                "${EventerizeApp.getInstance().getString(R.string.end_label)} ${endDateToFormat("dd/MM/yyyy à HH:mm")}"
    }
}