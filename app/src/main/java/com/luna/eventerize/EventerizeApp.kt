package com.luna.eventerize

import android.app.Application
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.Image
import com.luna.eventerize.data.repository.EventerizeRepo
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseObject


class EventerizeApp: Application() {

    lateinit var repository: EventerizeRepo

    override fun onCreate() {
        super.onCreate()
        app = this
        repository = EventerizeRepo()
        ParseObject.registerSubclass(Event::class.java)
        ParseObject.registerSubclass(Image::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("11558475925")
                .clientKey("154811896954758")
                .server("https://bigoud.games/eventerize/")
                .build()
        )
        ParseInstallation.getCurrentInstallation().saveInBackground()
    }





    companion object {
        private var app: EventerizeApp = EventerizeApp()

        fun getInstance(): EventerizeApp = app
    }
}