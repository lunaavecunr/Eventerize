package com.luna.eventerize.presentation.ui.fragments.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.viewmodel.factory.ViewModelFactory
import kotlin.reflect.KClass

abstract class BaseFragment<T : ViewModel>: Fragment() {

    var viewModelFactory: ViewModelFactory = ViewModelFactory()

    lateinit var viewModel: T

    abstract val viewModelClass: KClass<T>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(viewModelClass.java)
    }

}