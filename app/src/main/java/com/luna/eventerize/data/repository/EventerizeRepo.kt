package com.luna.eventerize.data.repository

import android.util.Log
import bolts.Task
import com.luna.eventerize.data.manager.ParseApi
import com.luna.eventerize.data.manager.ParseApiImpl
import com.parse.ParseUser

class EventerizeRepo {
    private var parseManager: ParseApi = ParseApiImpl()
    fun login(username: String, password: String) : Task<ParseUser> {
        return parseManager.login(username, password)

    }
}