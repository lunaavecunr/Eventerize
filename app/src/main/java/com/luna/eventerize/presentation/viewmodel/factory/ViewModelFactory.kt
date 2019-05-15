package com.luna.eventerize.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luna.eventerize.presentation.viewmodel.*
import com.luna.eventerize.presentation.viewmodel.createevent.CreateEventViewModel

class ViewModelFactory: ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
            return EventListViewModel() as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel() as T
        }
        if (modelClass.isAssignableFrom(TabsListViewModel::class.java)) {
            return TabsListViewModel() as T
        }
       if (modelClass.isAssignableFrom(CreateEventViewModel::class.java)) {
            return CreateEventViewModel() as T
        }
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel() as T
        }
        if (modelClass.isAssignableFrom(QRCodeViewModel::class.java)) {
            return QRCodeViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}