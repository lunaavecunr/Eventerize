package com.luna.eventerize.data.manager

import bolts.Task
import com.luna.eventerize.data.model.Event
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser

class ParseApiImpl : ParseApi {


    override fun login(username: String, password: String): Task<ParseUser> {
        return ParseUser.logInInBackground(username, password)
    }

    override fun signup(user: ParseUser): Task<Void> {
        return user.signUpInBackground()
    }

    override fun getEvent(): Task<List<Event>> {
        var query: ParseQuery<Event> = ParseQuery.getQuery("Event")
        query.include("members")
        query.include("images")
        return query.findInBackground()
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

    override fun <T: ParseObject> getRelation(relation: ParseRelation<T>): Task<List<T>> {
        return relation.query.findInBackground()
    }
}