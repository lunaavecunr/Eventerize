package com.luna.eventerize.data.model

import com.parse.ParseFile
import com.parse.ParseRelation
import com.parse.ParseUser
import java.util.*
import kotlin.collections.ArrayList

class Event(eventParse: EventParse) {
    var parse: EventParse = eventParse
    var title: String? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var location: String? = null
    var members: ArrayList<ParseUser>? = null
    var images: ArrayList<Image>? = null
    var logo: ParseFile? = null

    init {
        this.title = eventParse.name
        this.startDate = eventParse.startDate
        this.endDate = eventParse.endDate
        this.location = eventParse.location
        this.logo = eventParse.logo
    }
}