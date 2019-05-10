package com.luna.eventerize.presentation.navigator

import androidx.fragment.app.FragmentManager
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.EventDetailsFragment
import com.luna.eventerize.presentation.ui.fragments.CreateEventFragment
import com.luna.eventerize.presentation.ui.fragments.EventListFragment
import com.luna.eventerize.presentation.ui.fragments.LoginFragment
import com.luna.eventerize.presentation.ui.fragments.SignUpFragment
import com.luna.eventerize.presentation.ui.fragments.TabsListFragment

class Navigator(fragmentManager: FragmentManager) {

    private val fragmentManager: FragmentManager = fragmentManager

    fun displayTabsList(){
        fragmentManager.beginTransaction().replace(R.id.activity_auth_fragment, TabsListFragment.newInstance()).commit()
    }

    fun displayLogin() {
        fragmentManager.beginTransaction().replace(R.id.activity_auth_fragment, LoginFragment()).commit()
    }

    fun displayEventCreation() {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, CreateEventFragment.newInstance()).addToBackStack(null).commit()
    }

    fun displaySignUp() {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, SignUpFragment.newInstance()).addToBackStack(null).commit()
    }

    fun displayEventWithBackStack(backStack: Int){
        if(fragmentManager.findFragmentByTag(backStack.toString()) != null)
        {
            fragmentManager.beginTransaction().replace(R.id.fragment_tabs_list_frame_layout, fragmentManager.findFragmentByTag(backStack.toString())!!).commit()
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.fragment_tabs_list_frame_layout, EventListFragment.newInstance(backStack)).addToBackStack(backStack.toString()).commit()
        }
    }

    fun displayEventDetails(id:String) {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, EventDetailsFragment.newInstance(id)).addToBackStack(null).commit()
    }
}