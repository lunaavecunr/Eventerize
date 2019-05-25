package com.luna.eventerize.data.manager

import android.util.Log
import bolts.Task
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.Image
import com.luna.eventerize.presentation.utils.showError
import com.parse.*

class ParseApiImpl : ParseApi {
    override fun login(username: String, password: String): Task<ParseUser> {
        return ParseUser.logInInBackground(username, password)
    }

    override fun sessionTokenValid(sessionToken: String): Task<ParseUser> {
        return ParseUser.becomeInBackground(sessionToken)
    }

    override fun signup(user: ParseUser): Task<Void> {
        return user.signUpInBackground()
    }

    override fun getEvent(): Task<List<Event>> {
        var query: ParseQuery<Event> = ParseQuery.getQuery("Event")
        query.whereEqualTo("members", ParseUser.getCurrentUser())
        var query2: ParseQuery<Event> = ParseQuery.getQuery("Event")
        query2.whereEqualTo("owner", ParseUser.getCurrentUser())
        var listQueries = ArrayList<ParseQuery<Event>>()
        listQueries.add(query)
        listQueries.add(query2)
        var finalQuery = ParseQuery.or(listQueries)
        finalQuery.include("members")
        finalQuery.include("images")
        return finalQuery.findInBackground()
    }

    override fun getEventByOwner(): Task<List<Event>> {
        var query: ParseQuery<Event> = ParseQuery.getQuery("Event")
        query.include("members")
        query.include("images")
        query.whereEqualTo("owner", ParseUser.getCurrentUser())
        return query.findInBackground()
    }

    override fun getEventByMembers(): Task<List<Event>> {
        var query: ParseQuery<Event> = ParseQuery.getQuery("Event")
        query.include("members")
        query.include("images")
        query.whereEqualTo("members", ParseUser.getCurrentUser())
        return query.findInBackground()
    }

    override fun getEventById(id: String): Task<Event> {
        val query: ParseQuery<Event> = ParseQuery.getQuery("Event")
        query.include("members")
        query.include("images")
        query.include("owner")
        return query.getInBackground(id)
    }


    override fun saveEvent(event:Event): Task<Void> {
        return event.saveInBackground()
    }
}