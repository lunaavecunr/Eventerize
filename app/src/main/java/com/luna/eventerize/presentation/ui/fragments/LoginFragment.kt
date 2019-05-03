package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.LoginViewModel
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment<LoginViewModel>(), View.OnClickListener {

    override val viewModelClass = LoginViewModel::class
    lateinit var navigator: Navigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = getString(R.string.login_title)
        navigator = Navigator(fragmentManager!!)
        fragment_login_loginButton.setOnClickListener(this)
        fragment_login_createAccountTV.setOnClickListener(this)

        val updateError = Observer<EventerizeError> {
            showError(activity!!, it.message)
        }

        val updateUser = Observer<ParseUser> {
            TODO()
        }

        val updateEvent = Observer<ArrayList<Event>> {
            for (event in it) {
                Log.d("mlk", "Title: " + event.title)
                Log.d("mlk", "StartDate: " + event.startDate)
                Log.d("mlk", "EndDate: " + event.endDate)
                Log.d("mlk", "Location: " + event.location)
                Log.d("mlk", "Logo: " + event.logo!!.url)
            }
            viewModel.getMembersEvent()
        }

        val updateEventImage = Observer<ArrayList<Event>> {
            for (event in it) {
                for (image in event.images!!) {
                    Log.d("mlk", "URL: "+image.file!!.url)
                    Log.d("mlk", "ID user: "+image.user!!.objectId)
                    Log.d("mlk", "string: "+image.string)
                }
            }
        }

        val updateEventMembers = Observer<ArrayList<Event>> {
            for (event in it) {
               for(user in event.members!!){
                   Log.d("mlk", "id User: "+user.objectId)
                   Log.d("mlk", "username: "+user.username)
               }
            }
            viewModel.getImageEvent()
        }

        viewModel.getEventsMembersRetrivial().observe(this,updateEventMembers)
        viewModel.getEventsImageRetrivial().observe(this,updateEventImage)
        viewModel.getEventsRetrivial().observe(this, updateEvent)
        viewModel.getError().observe(this, updateError)
        viewModel.getUser().observe(this, updateUser)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fragment_login_loginButton -> {
//                viewModel.login(fragment_login_emailField.text.toString(), fragment_login_passwordField.text.toString())
                viewModel.getEvent()
            }
            R.id.fragment_login_createAccountTV -> {
                navigator.displaySignUp()
            }

        }
    }


}