package com.luna.eventerize.data.manager

import bolts.Task
import com.parse.ParseUser

class ParseApiImpl : ParseApi {
    override fun signup(user: ParseUser): Task<Void> {
        return user.signUpInBackground()
    }
}