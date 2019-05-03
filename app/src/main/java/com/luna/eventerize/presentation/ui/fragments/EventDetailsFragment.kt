package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.EventDetailViewModel
import com.luna.eventerize.presentation.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_event_details.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlin.reflect.KClass

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EventDetailsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EventDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EventDetailsFragment : BaseFragment<EventDetailViewModel>(), View.OnClickListener {
    override val viewModelClass = EventDetailViewModel::class

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initToolbar(){
        activity!!.title = getString(R.string.event_details_title)
        event_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initMainInfos(){
        viewModel.eventDetail.observe(this, Observer {
            participant_number.setText(viewModel.formatText(it.participantNumber," participants","s"))
        })
    }
}
