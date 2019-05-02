package com.luna.eventerize

import android.app.Application
import com.luna.eventerize.data.repository.EventerizeRepo

class EventerizeApp: Application() {

    lateinit var repository: EventerizeRepo

    override fun onCreate() {
        super.onCreate()
        app = this
        repository = EventerizeRepo()
    }



    companion object {
        private var app: EventerizeApp = EventerizeApp()

        fun getInstance(): EventerizeApp = app
    }
}