package com.luna.eventerize.data.manager

import bolts.Task
import com.parse.ParseUser

interface ParseApi {
    fun signup(user: ParseUser): Task<Void>
}