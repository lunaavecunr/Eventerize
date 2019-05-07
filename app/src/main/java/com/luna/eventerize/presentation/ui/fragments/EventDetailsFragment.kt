package com.luna.eventerize.presentation.ui.fragments

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager

import com.luna.eventerize.R
import com.luna.eventerize.data.model.Event
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.EventDetailsAdapter
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.EventDetailViewModel
import kotlinx.android.synthetic.main.fragment_event_details.*

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

        event = Event(13,"Rue de la Fraternité","13/06/1983","16/06/1987","BRUNON",
            ContextCompat.getDrawable(context!!,R.drawable.ic_calendar)!!,
            arrayListOf(),"Marriage")

        //Toolbar
        navigator = Navigator(fragmentManager!!)
        activity!!.title = "${event!!.title} - ${getString(R.string.event_details_title)}"
        event_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(event_detail_toolbar)

        initOnClickListener()

        initInfos()

        initRecyclerView()
    }

    private fun initInfos(){
        participant_number.text = "${event!!.participantNumber} ${if(event!!.participantNumber<2){getString(R.string.participant_label_single)}else{getString(R.string.participant_label_plural)}}"
        location_label.text = "${event!!.locationEvent}"
        event_date_label.text = "Du ${event!!.beginEvent} au ${event!!.endingEvent}"
        supervisor_label.text = event!!.supervisor
        event_detail_event_logo.setImageDrawable(event!!.logo)
    }

    private fun initOnClickListener(){
        participant_number.setOnClickListener(this)
    }

    private fun initRecyclerView(){
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event!!.galleryList.add("http://noudjou.free.fr/images/galeries/baleines/souffle/event.jpg")
        event_details_recycler_view.layoutManager = GridLayoutManager(context,3)
        event_details_recycler_view.adapter = EventDetailsAdapter(event!!.galleryList)
    }

    companion object{
        fun newInstance() = EventDetailsFragment()
    }
}
