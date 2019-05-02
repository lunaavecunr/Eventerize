package com.luna.eventerize.data.model

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Event")
class Event(
    var title: String,
    var date: String,
    var place: String,
    var image: Int
) : ParseObject(title) {

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("title", title)
        outState?.putString("date", date)
        outState?.putString("place", place)
        outState?.putInt("image", image)
    }

    override fun onRestoreInstanceState(savedState: Bundle?) {
        this.title = savedState?.getString("title")!!
        this.date = savedState.getString("date")!!
        this.place = savedState.getString("place")!!
        this.image = savedState.getInt("image")!!
    }
}