package com.luna.eventerize.data.manager

import bolts.Task
import com.luna.eventerize.data.model.EventParse
import com.parse.ParseObject
import com.parse.ParseRelation
import com.parse.ParseUser
interface ParseApi {
    fun login(username: String, password: String): Task<ParseUser>
    fun signup(user: ParseUser): Task<Void>
    fun getEvent(): Task<List<EventParse>>
    fun getEventById(id: String): Task<EventParse>
    fun <T: ParseObject> getRelation(relation: ParseRelation<T>): Task<List<T>>
}