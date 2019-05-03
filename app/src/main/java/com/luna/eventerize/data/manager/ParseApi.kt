package com.luna.eventerize.data.manager

import bolts.Task
import com.luna.eventerize.data.model.Event
import com.parse.ParseUser
interface ParseApi {
    fun login(username: String, password: String): Task<ParseUser>
    fun signup(user: ParseUser): Task<Void>
    fun getEvent(): Task<List<Event>>
}