package com.luna.eventerize.data.manager

import bolts.Task
import com.luna.eventerize.data.model.Event
import com.parse.ParseQuery
import com.parse.ParseUser

class ParseApiImpl : ParseApi {
    override fun login(username: String, password: String): Task<ParseUser> {
        return ParseUser.logInInBackground(username, password)
    }

    override fun signup(user: ParseUser): Task<Void> {
        return user.signUpInBackground()
    }

    override fun getEvent(): Task<List<Event>> {
        var query: ParseQuery<Event> = ParseQuery.getQuery(Event.cla)
        return query.findInBackground()
    }
}