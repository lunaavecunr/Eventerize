package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.TabsListAdapter
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.TabsListViewModel
import kotlinx.android.synthetic.main.fragment_tabs_list.*


class TabsListFragment : BaseFragment<TabsListViewModel>() {
    private lateinit var adapter: TabsListAdapter
    private lateinit var navigator: Navigator
    override val viewModelClass = TabsListViewModel::class

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = TabsListAdapter(fragmentManager!!)
        adapter.addFragment(EventListFragment.newInstance("all"), "Tous")
        adapter.addFragment(EventListFragment.newInstance("orga"), "Organisateur")
        adapter.addFragment(EventListFragment.newInstance("member"), "Membre")


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        activity!!.title = getString(R.string.fragment_list_event_title)
        (activity as AppCompatActivity).setSupportActionBar(fragment_tabs_list_toolbar)

        fragment_tabs_list_viewpager.adapter = adapter
        fragment_tabs_list_tabs.setupWithViewPager(fragment_tabs_list_viewpager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tabs_list, container, false)
    }

    companion object {
        fun newInstance(): TabsListFragment = TabsListFragment()
    }
}