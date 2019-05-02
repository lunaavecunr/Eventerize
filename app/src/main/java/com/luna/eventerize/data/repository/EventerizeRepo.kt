package com.luna.eventerize.data.repository

import android.util.Log
import bolts.Task
import com.luna.eventerize.data.manager.ParseApi
import com.luna.eventerize.data.manager.ParseApiImpl
import com.parse.ParseUser

class EventerizeRepo {
    private var parseManager: ParseApi = ParseApiImpl()
//    fun login(username: String, password: String) {
//        parseManager.login(username, password)
//            .continueWith {
//                when {
//                    it.isCancelled -> {
//                        // the save was cancelled.
//                    }
//                    it.isFaulted -> {
//                        Log.d("mlk", "Faulted: " + it.error.message)
//                    } // the save failed
//                    else -> {
//                        Log.d("mlk", "Success: " + it.result.toString())
//                    }
//                }
//            }
//    }
}