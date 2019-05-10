package com.luna.eventerize.presentation.navigator

import androidx.fragment.app.FragmentManager
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.datawrapper.ImageWrapper
import com.luna.eventerize.presentation.ui.fragments.*

class Navigator(fragmentManager: FragmentManager) {

    private val fragmentManager: FragmentManager = fragmentManager

    fun displayEventList(){
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

    fun displayEventDetails(id:String) {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, EventDetailsFragment.newInstance(id)).addToBackStack(null).commit()
    }

    fun displayPhoto(imageURL:String, photoId:String, eventId:String) {
        fragmentManager.beginTransaction().setCustomAnimations(R.animator.pop_fragment, R.animator.pop_out_fragment).replace(R.id.activity_auth_fragment, PhotoDetailFragment.newInstance(imageURL,photoId,eventId)).addToBackStack(null).commit()
    }
}