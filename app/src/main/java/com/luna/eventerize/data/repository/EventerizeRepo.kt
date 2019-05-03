package com.luna.eventerize.data.repository

import bolts.Task
import com.luna.eventerize.data.manager.ParseApi
import com.luna.eventerize.data.manager.ParseApiImpl
import com.luna.eventerize.data.model.Event
import com.parse.ParseUser

class EventerizeRepo {
    private var parseManager: ParseApi = ParseApiImpl()
    fun login(username: String, password: String) : Task<ParseUser> {
        return parseManager.login(username, password)

    }
    fun signup(user: ParseUser) : Task<Void> {
       return parseManager.signup(user)
    }

    fun getEvent(): Task<List<Event>> {
        return parseManager.getEvent()
    }
}