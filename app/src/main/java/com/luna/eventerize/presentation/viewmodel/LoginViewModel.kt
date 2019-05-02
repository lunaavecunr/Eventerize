package com.luna.eventerize.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.data.repository.EventerizeRepo
import com.parse.ParseUser

class LoginViewModel: ViewModel() {
    var repository = EventerizeRepo()
    var error = MutableLiveData<EventerizeError>()
    var user = MutableLiveData<ParseUser>()

    fun login(username : String, password : String) {
        if(username.isBlank() || password.isBlank()) {
            error.postValue(EventerizeError("Veuillez remplir tout les champs", "Erreur de connexion"))
            return
        }
        repository.login(username, password)
            .continueWith {
                when {
                    it.isCancelled -> {
                        error.postValue(EventerizeError("La connexion a échoué", "Erreur de connexion"))
                    }
                    it.isFaulted -> {
                        error.postValue(EventerizeError(it.error.message.toString(), "Erreur de connexion"))
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