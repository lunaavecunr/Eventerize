package com.luna.eventerize.presentation.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.LoginViewModel
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException


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
        if (ParseUser.getCurrentSessionToken() != null) {
           viewModel.sessionTokenValid(ParseUser.getCurrentSessionToken())
        }
        fragment_login_loginButton.setOnClickListener(this)
        fragment_login_createAccountTV.setOnClickListener(this)

        val updateError = Observer<EventerizeError> {
            showError(activity!!, it.message)
        }

        val updateUser = Observer<ParseUser> {

            navigator.displayTabsList()
        }
        viewModel.getError().observe(this, updateError)
        viewModel.getUser().observe(this, updateUser)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fragment_login_loginButton -> {
                viewModel.login(fragment_login_emailField.text.toString(), fragment_login_passwordField.text.toString())
           }
           R.id.fragment_login_createAccountTV -> {
               navigator.displaySignUp()
           }

       }
    }


}