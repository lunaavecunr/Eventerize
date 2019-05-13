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

    fun destroyImage(imageId:String){
        parseManager.deleteImage(imageId)
    }

    fun signup(user: ParseUser) : Task<Void> {
       return parseManager.signup(user)
    }

    fun getEvent(): Task<List<Event>> {
        return parseManager.getEvent()
    }

    fun getEventByOwner(): Task<List<Event>> {
        return parseManager.getEventByOwner()
    }

    fun getEventByMembers(): Task<List<Event>> {
        return parseManager.getEventByMembers()
    }

    fun saveEvent(event: Event): Task<Void>{
        return parseManager.saveEvent(event)
    }
    fun getEventById(id:String): Task<Event>{
        return parseManager.getEventById(id)
    }

    fun sessionTokenValid(sessionToken: String): Task<ParseUser> {
        return parseManager.sessionTokenValid(sessionToken)
    }
}