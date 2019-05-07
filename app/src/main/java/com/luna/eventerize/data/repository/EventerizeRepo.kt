package com.luna.eventerize.data.repository

import bolts.Task
import com.luna.eventerize.data.manager.ParseApi
import com.luna.eventerize.data.manager.ParseApiImpl
import com.luna.eventerize.data.model.EventParse
import com.parse.ParseObject
import com.parse.ParseRelation
import com.parse.ParseUser

class EventerizeRepo {
    private var parseManager: ParseApi = ParseApiImpl()
    fun login(username: String, password: String) : Task<ParseUser> {
        return parseManager.login(username, password)

    }
    fun signup(user: ParseUser) : Task<Void> {
       return parseManager.signup(user)
    }

    fun getEvent(): Task<List<EventParse>> {
        return parseManager.getEvent()
    }

    fun getEventById(id: String): Task<EventParse> {
        return parseManager.getEventById(id)
    }

    fun <T: ParseObject> getRelation(relation: ParseRelation<T>): Task<List<T>> {
        return parseManager.getRelation(relation)
    }
}