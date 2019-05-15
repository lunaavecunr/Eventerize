package com.luna.eventerize.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.GalleryAdapter
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.luna.eventerize.presentation.ui.datawrapper.ImageWrapper
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.EventDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event_details.*


private const val INTENT_DETAILS_ID_EXTRA = "INTENT_DETAILS_ID_EXTRA"

class EventDetailsFragment : BaseFragment<EventDetailViewModel>(), View.OnClickListener {
    override val viewModelClass = EventDetailViewModel::class
    private lateinit var navigator: Navigator
    private lateinit var adapter: GalleryAdapter
    private lateinit var eventWrap: EventWrapper

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.participant_number -> {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_event_details, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
            R.id.menu_event_details_share -> {
                navigator.displayShare(eventWrap.event.objectId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = GalleryAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getEventById(arguments?.getString(INTENT_DETAILS_ID_EXTRA)!!)

        //Toolbar
        navigator = Navigator(fragmentManager!!)
        event_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(event_detail_toolbar)

        event_details_picture_gallery_recycler_view.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        event_details_picture_gallery_recycler_view.adapter = adapter

        val updateEvent = Observer<EventWrapper> {
            eventWrap = it
            showEvent(it)
        }
        val updateGallery = Observer<List<ImageWrapper>> {
            adapter.updateImageList(it)
            if (it.isNullOrEmpty()) {
                event_detail_no_image_in_gallery.visibility = View.VISIBLE
            } else {
                event_detail_no_image_in_gallery.visibility = View.INVISIBLE
            }
        }

        viewModel.getEvent().observe(this, updateEvent)
        viewModel.getGallery().observe(this, updateGallery)
    }

    private fun showEvent(eventWrapper: EventWrapper) {
        participant_number.text = eventWrapper.numberOfMembers()
        location_label.text = eventWrapper.event.location
        event_date_label.text = eventWrapper.dateCoverLabel()
        supervisor_label.text = eventWrapper.event.owner!!.username

        if (eventWrapper.event.logo != null) {
            Picasso.get().load(eventWrapper.event.logo!!.url).into(event_detail_event_logo)
        } else {
            Picasso.get().load(R.mipmap.eventerize).into(event_detail_event_logo)
        }
        event_detail_no_image_in_gallery_logo.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.mipmap.eventerize
            )
        )

        activity!!.title = eventWrapper.event.title

        //OnClickListener
        participant_number.setOnClickListener(this)

        val imageWrapperList: MutableList<ImageWrapper> = mutableListOf()

        if (eventWrapper.event.images != null) {
            eventWrapper.event.images!!.map {
                imageWrapperList.add(ImageWrapper(it))
            }
        }

        viewModel.updateImageGallery(imageWrapperList)

    }

    companion object {
        fun newInstance(identifier: String = ""): EventDetailsFragment {
            val fragment = EventDetailsFragment()
            val args = Bundle()
            args.putString(INTENT_DETAILS_ID_EXTRA, identifier)
            fragment.arguments = args
            return fragment
        }
    }
}
