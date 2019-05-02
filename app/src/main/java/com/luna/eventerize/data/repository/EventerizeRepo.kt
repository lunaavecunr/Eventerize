package com.luna.eventerize.data.repository

import bolts.Task
import com.luna.eventerize.data.manager.ParseApi
import com.luna.eventerize.data.manager.ParseApiImpl
import com.parse.ParseUser


class EventerizeRepo {
    private val parseManager: ParseApi = ParseApiImpl()

    fun signup(user: ParseUser) : Task<Void> {
       return parseManager.signup(user)
    }
}