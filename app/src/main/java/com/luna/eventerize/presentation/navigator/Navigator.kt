package com.luna.eventerize.presentation.navigator

import androidx.fragment.app.FragmentManager
import com.luna.eventerize.presentation.ui.fragments.LoginFragment
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.signup.SignUpFragment

class Navigator(fragmentManager: FragmentManager) {

    private val fragmentManager: FragmentManager = fragmentManager

    fun displayLogin() {
        fragmentManager.beginTransaction().replace(R.id.constraintLayout, LoginFragment()).commit()
    }

    fun displaySignUp() {
        fragmentManager.beginTransaction().replace(R.id.activity_auth_fragment, SignUpFragment.newInstance()).addToBackStack(null).commit()
    }
}