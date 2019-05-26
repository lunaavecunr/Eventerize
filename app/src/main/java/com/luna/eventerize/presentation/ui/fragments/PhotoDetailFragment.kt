package com.luna.eventerize.presentation.ui.fragments

import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.luna.eventerize.presentation.ui.datawrapper.ImageWrapper
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.ImageDownloader
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.PhotoDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_photo_detail.*
import java.io.File

private const val PHOTO_URL = "PHOTO_URL"
private const val PHOTO_ID = "PHOTO_ID"
private const val EVENT_ID = "EVENT_ID"
private const val EVENT_NAME = "EVENT_NAME"
private const val PHOTO_NAME = "PHOTO_NAME"

class PhotoDetailFragment : BaseFragment<PhotoDetailViewModel>() {
    private var photoUrl: String? = null
    private var photoId: String? = null
    private var eventId: String? = null
    private var eventName : String? = null
    private var photoName: String? = null
    private var navigator: Navigator? = null
    override val viewModelClass = PhotoDetailViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoUrl = it.getString(PHOTO_URL)
            photoId = it.getString(PHOTO_ID)
            eventId = it.getString(EVENT_ID)
            eventName = it.getString(EVENT_NAME)
            photoName = it.getString(PHOTO_NAME)
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
            R.id.photo_detail_menu_download_photo -> {
                val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

                ImageDownloader().downloadImage(photoUrl!!, photoName!!, dirPath, eventName, context!!)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        fun newInstance(photoUrl: String = "", photoId: String = "", eventObjectId: String = "", eventName: String = "", imageName: String = ""): PhotoDetailFragment {
            val fragment = PhotoDetailFragment()
            val args = Bundle()
            args.putString(PHOTO_URL, photoUrl)
            args.putString(PHOTO_ID, photoId)
            args.putString(EVENT_ID, eventObjectId)
            args.putString(EVENT_NAME, eventName)
            args.putString(PHOTO_NAME, imageName)
            fragment.arguments = args
            return fragment
        }
    }
}
