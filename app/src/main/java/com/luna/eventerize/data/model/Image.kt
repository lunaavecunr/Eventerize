package com.luna.eventerize.data.model

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Image")
class Image: ParseObject() {
    fun getString(): String?{
        return getString("string")
    }

    var file: ParseFile?
        set(value) {
            put("file", value!!)
        }
        get() {
            return getParseFile("file")
        }

    var user: ParseUser?
        set(value) {
            put("user", value!!)
        }
        get() {
            return getParseUser("user")
        }
}