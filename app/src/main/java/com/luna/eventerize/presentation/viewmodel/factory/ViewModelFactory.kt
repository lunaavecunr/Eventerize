package com.luna.eventerize.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luna.eventerize.presentation.viewmodel.EventDetailViewModel
import com.luna.eventerize.presentation.viewmodel.LoginViewModel
import com.luna.eventerize.presentation.viewmodel.SignUpViewModel

class ViewModelFactory: ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel() as T
        }
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
