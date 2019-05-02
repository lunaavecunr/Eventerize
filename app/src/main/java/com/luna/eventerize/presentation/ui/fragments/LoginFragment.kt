package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment<LoginViewModel>() {
    override val viewModelClass = LoginViewModel::class

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_login_loginButton.setOnClickListener {
            verifyFieldsAndLogin()
        }


    }

    fun verifyFieldsAndLogin() {
        if(fragment_login_emailField.text!!.isEmpty() || fragment_login_passwordField.text!!.isEmpty()){
            Toast.makeText(context, getString(R.string.fillAllFields), Toast.LENGTH_SHORT).show()
        } else {

        }
    }

}