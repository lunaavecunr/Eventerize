package com.luna.eventerize.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.parse.ParseUser

class SignUpViewModel : ViewModel() {

    var error = MutableLiveData<EventerizeError>()

    var success = MutableLiveData<Boolean>()

    var repository = EventerizeApp.getInstance().repository

    fun signup(email: String, username: String, password: String, confirmPassword: String, cgu: Boolean) {
        if (email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.signup_error_field_empty), EventerizeApp.getInstance().getString(R.string.signup_error)))
            return
        }
        if (!cgu) {
            error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.signup_error_cgu), EventerizeApp.getInstance().getString(R.string.signup_error)))
            return
        }
        if (password != confirmPassword) {
            error.postValue(EventerizeError(EventerizeApp.getInstance().getString(R.string.signup_error_password), EventerizeApp.getInstance().getString(R.string.signup_error)))
            return
        }
        var user : ParseUser = ParseUser()
        user.email = email
        user.username = username
        user.setPassword(password)
        repository.signup(user).continueWith {
            when {
                it.isCancelled -> {
                    // the save was cancelled.
                    error.postValue(EventerizeError("La sauvegarde a échouée", "Erreur d'inscription"))
                }
                it.isFaulted -> {
                    error.postValue(EventerizeError(it.error.message.toString(), "Erreur d'inscription"))
                } // the save failed
                else -> {
                    Log.d("mlk", it.result.toString())
                    success.postValue(true)
                }
            }
        }
    }

    fun getError(): LiveData<EventerizeError> = error

    fun getSuccess(): LiveData<Boolean> = success
}