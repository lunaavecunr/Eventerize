package com.luna.eventerize.presentation.navigator

import androidx.fragment.app.FragmentManager
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.EventListFragment

class Navigator(fragmentManager: FragmentManager) {

    private val fragmentManager: FragmentManager = fragmentManager

    fun displayEventList(){
        /*
        *
        * A REMPLACER
        *
        * */
        fragmentManager.beginTransaction().replace(R.id.fragment_event_list_recycler_view, EventListFragment.newInstance()).commit()
    }

    fun createList(){

    }
}