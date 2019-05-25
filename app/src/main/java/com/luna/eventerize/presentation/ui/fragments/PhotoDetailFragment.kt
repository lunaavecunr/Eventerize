package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.PhotoDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo_detail.*

private const val PHOTO_URL = "PHOTO_URL"
private const val PHOTO_ID = "PHOTO_ID"
private const val EVENT_ID = "EVENT_ID"

class PhotoDetailFragment : BaseFragment<PhotoDetailViewModel>() {
    private var photoUrl: String? = null
    private var photoId: String? = null
    private var eventId: String? = null
    private var navigator: Navigator? = null
    override val viewModelClass = PhotoDetailViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoUrl = it.getString(PHOTO_URL)
            photoId = it.getString(PHOTO_ID)
            eventId = it.getString(EVENT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_photo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigator(fragmentManager!!)
        activity!!.title = ""
        fragment_photo_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(fragment_photo_detail_toolbar)

        Picasso.get().load(photoUrl).into(fragment_photo_detail_photo_view)

        val updateDeletingOperation = Observer<Boolean> {
            if (it) {
                activity?.onBackPressed()
            }
        }

        val updateError = Observer<EventerizeError> {
            showError(activity!!, it.message)
        }


        viewModel.getSuccessDeletingOperation().observe(this, updateDeletingOperation)
        viewModel.getError().observe(this, updateError)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.photo_details_fragment_options_menu, menu)
    }

    private fun deletePicture() {
        val dialog = AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.photo_destroy_operation))
            .setMessage(getString(R.string.photo_destroy_operation_text))
            .setPositiveButton(
                getString(R.string.yes_destroy_it)
            ) { _, _ ->
                viewModel.destroyPicture(eventId!!, photoId!!)
            }
            .setNeutralButton(
                getString(R.string.i_prefer_not_to)
            ) { _, _ ->
            }
        dialog.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            R.id.photo_detail_menu_delete_photo -> {
                deletePicture()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        fun newInstance(photoUrl: String = "", photoId: String = "", eventObjectId: String = ""): PhotoDetailFragment {
            val fragment = PhotoDetailFragment()
            val args = Bundle()
            args.putString(PHOTO_URL, photoUrl)
            args.putString(PHOTO_ID, photoId)
            args.putString(EVENT_ID, eventObjectId)
            fragment.arguments = args
            return fragment
        }
    }
}
