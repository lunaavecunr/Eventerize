package com.luna.eventerize.data.manager

import bolts.Task
import com.parse.ParseUser
interface ParseApi {
    fun login(username: String, password: String): Task<ParseUser>
    fun signup(user: ParseUser): Task<Void>
}