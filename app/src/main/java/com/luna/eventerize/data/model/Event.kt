package com.luna.eventerize.data.model

import com.parse.*
import java.util.*

@ParseClassName("Event")
class Event : ParseObject() {
    var title: String?
        set(value) {
            put("title", value!!)
        }
        get() {
            return getString("title")
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

    var members: List<ParseUser>?
        set(value) {
            put("members", value!!)
        }
        get() {
            return getList<ParseUser>("members")
        }

    var images: List<Image>?
        set(value) {
            put("images", value!!)
        }
        get() {
            return getList<Image>("images")
        }

    var logo: ParseFile?
        set(value) {
            put("logo", value!!)
        }
        get() {
            return getParseFile("logo")
        }

    var owner: ParseUser?
        set(value) {
            put("owner", value!!)
        }
        get() {
            return getParseUser("owner")
        }
}