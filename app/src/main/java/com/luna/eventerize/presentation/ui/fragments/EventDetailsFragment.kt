package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.EventDetailsAdapter
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.EventDetailViewModel
import com.parse.ParseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event_details.*
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsFragment : BaseFragment<EventDetailViewModel>(), View.OnClickListener {
    override val viewModelClass = EventDetailViewModel::class
    private var event:Event? = null
    private lateinit var navigator: Navigator

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.participant_number -> {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Toolbar
        navigator = Navigator(fragmentManager!!)
        activity!!.title = "${event!!.title} - ${getString(R.string.event_details_title)}"
        event_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(event_detail_toolbar)

        event = Event()

        initOnClickListener()

        initInfos()

        initRecyclerView()
    }

    private fun formatDate(input:Date):String{
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)
        return format.format(input)
    }

    private fun initInfos(){
        participant_number.text = "${event!!.members!!.size} ${if(event!!.members!!.size<2){getString(R.string.participant_label_single)}else{getString(R.string.participant_label_plural)}}"
        location_label.text = "${event!!.location}"
        event_date_label_begin.text = "${getString(R.string.begin_label)} ${formatDate(event!!.startDate!!)} ${getString(R.string.end_label)} ${formatDate(event!!.endDate!!)}"
        supervisor_label.text = event!!.owner!!.email
        Picasso.get().load(event!!.logo!!.url).into(event_detail_event_logo)
    }

    private fun initOnClickListener(){
        participant_number.setOnClickListener(this)
    }

    private fun initRecyclerView(){
        event_details_recycler_view.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        event_details_recycler_view.adapter = EventDetailsAdapter(event!!)
    }

    companion object{
        fun newInstance() = EventDetailsFragment()
    }
}
