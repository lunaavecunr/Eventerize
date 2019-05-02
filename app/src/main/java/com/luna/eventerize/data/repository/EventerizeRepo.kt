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

/*
.continueWith {
    when {
        it.isCancelled -> {
            // the save was cancelled.
        }
        it.isFaulted -> {
            Log.d("mlk", it.error.message)
        } // the save failed
        else -> {
            Log.d("mlk", it.result.toString())
        }
    }
}*/
