package com.luna.eventerize.presentation.navigator

import androidx.fragment.app.FragmentManager
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.EventDetailsFragment
import com.luna.eventerize.presentation.ui.fragments.EventListFragment
import com.luna.eventerize.presentation.ui.fragments.LoginFragment
import com.luna.eventerize.presentation.ui.fragments.SignUpFragment

class Navigator(fragmentManager: FragmentManager) {

    private val fragmentManager: FragmentManager = fragmentManager

    fun displayEventList(){
        fragmentManager.beginTransaction().replace(R.id.activity_auth_fragment, EventListFragment.newInstance()).commit()
    }

    fun displayLogin() {
        fragmentManager.beginTransaction().replace(R.id.activity_auth_fragment, LoginFragment()).commit()
    }

    fun displaySignUp() {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, SignUpFragment.newInstance()).addToBackStack(null).commit()
    }

    fun displayEventDetails() {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, EventDetailsFragment.newInstance()).addToBackStack(null).commit()
    }
}