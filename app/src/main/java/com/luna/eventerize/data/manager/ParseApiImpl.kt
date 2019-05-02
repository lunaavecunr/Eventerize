package com.luna.eventerize.data.manager

import bolts.Task
import com.parse.ParseUser

class ParseApiImpl : ParseApi {
    override fun login(username: String, password: String): Task<ParseUser> {
        return ParseUser.logInInBackground(username, password)
    }

}