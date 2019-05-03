package com.luna.eventerize.data.model

import com.parse.*
import java.util.*

@ParseClassName("Event")
class EventParse : ParseObject() {
    var name: String?
        set(value) {
            put("name", value!!)
        }
        get() {
            return getString("name")
        }

    var startDate: Date?
        set(value) {
            put("startDate", value!!)
        }
        get() {
            return getDate("startDate")
        }

    var endDate: Date?
        set(value) {
            put("endDate", value!!)
        }
        get() {
            return getDate("endDate")
        }

    var location: String?
        set(value) {
            put("location", value!!)
        }
        get() {
            return getString("location")
        }

    var members: ParseRelation<ParseUser>
        set(value) {
            put("members", value!!)
        }
        get() {
            return getRelation("members")
        }

    var images: ParseRelation<ImageParse>
        set(value) {
            put("images", value!!)
        }
        get() {
            return getRelation("images")
        }

    var logo: ParseFile?
        set(value) {
            put("logo", value!!)
        }
        get() {
            return getParseFile("logo")
        }

}