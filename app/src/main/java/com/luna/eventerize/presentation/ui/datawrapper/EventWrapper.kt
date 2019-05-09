package com.luna.eventerize.presentation.ui.datawrapper

import com.google.android.material.textfield.TextInputEditText
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
}