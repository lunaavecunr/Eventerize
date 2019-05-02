package com.luna.eventerize.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.data.repository.EventerizeRepo
import com.parse.ParseUser

class LoginViewModel: ViewModel() {
    var repository = EventerizeApp.getInstance().repository
    var error = MutableLiveData<EventerizeError>()
    var user = MutableLiveData<ParseUser>()

    fun login(username : String, password : String) {
        if(username.isBlank() || password.isBlank()) {
            error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.fill_all_fields), EventerizeApp.getInstance().getString(R.string.login_error_title)))
            return
        }
        repository.login(username, password)
            .continueWith {
                when {
                    it.isCancelled -> {
                        error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.login_connection_failed), EventerizeApp.getInstance().getString(R.string.login_error_title)))
                    }
                    it.isFaulted -> {
                        error.postValue(EventerizeError(it.error.message.toString(), EventerizeApp.getInstance().getString(R.string.login_error_title)))
                    }
                    else -> {
                        user.postValue(it.result)
                    }
                }
            }
    }

    fun getError() : LiveData<EventerizeError> = error
    fun getUser() : LiveData<ParseUser> = user
}