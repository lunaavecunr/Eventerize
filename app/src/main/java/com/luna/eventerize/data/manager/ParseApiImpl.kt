package com.luna.eventerize.data.manager

import bolts.Task
import com.luna.eventerize.data.model.EventParse
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

    override fun getEvent(): Task<List<EventParse>> {
        var query: ParseQuery<EventParse> = ParseQuery.getQuery("Event")
        return query.findInBackground()
    }

    override fun <T: ParseObject> getRelation(relation: ParseRelation<T>): Task<List<T>> {
        return relation.query.findInBackground()
    }
}