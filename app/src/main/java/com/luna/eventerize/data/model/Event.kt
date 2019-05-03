package com.luna.eventerize.data.model

import android.os.Bundle
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseRelation

@ParseClassName("Event")
class Event(
    var name: String,
    var date: String,
    var place: String,
    var image: ParseRelation<Image>
) : ParseObject() {
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("name", name)

    }
    override fun onRestoreInstanceState(savedState: Bundle?) {
        this.name = savedState?.getString("name")!!

    }
}