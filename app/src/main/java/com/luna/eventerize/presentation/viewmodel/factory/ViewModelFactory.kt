package com.luna.eventerize.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.luna.eventerize.presentation.viewmodel.EventListViewModel

class ViewModelFactory: ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
            return EventListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}