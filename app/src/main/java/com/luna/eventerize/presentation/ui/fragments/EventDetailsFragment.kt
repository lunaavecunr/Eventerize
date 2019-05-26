package com.luna.eventerize.presentation.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.adapter.GalleryAdapter
import com.luna.eventerize.presentation.ui.datawrapper.EventWrapper
import com.luna.eventerize.presentation.ui.datawrapper.ImageWrapper
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.ImageDownloader
import com.luna.eventerize.presentation.viewmodel.EventDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event_details.*


private const val INTENT_DETAILS_ID_EXTRA = "INTENT_DETAILS_ID_EXTRA"


class EventDetailsFragment : BaseFragment<EventDetailViewModel>(), View.OnClickListener {
    override val viewModelClass = EventDetailViewModel::class
    private lateinit var navigator: Navigator
    private lateinit var adapter: GalleryAdapter
    private lateinit var galleryWrapper : List<ImageWrapper>
    private lateinit var eventWrapper : EventWrapper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = GalleryAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Toolbar
        navigator = Navigator(fragmentManager!!)
        event_detail_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(event_detail_toolbar)

        event_details_picture_gallery_recycler_view.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        event_details_picture_gallery_recycler_view.adapter = adapter
        adapter.setOnImageClick { displayImage(it) }


        val updateEvent = Observer<EventWrapper> {
            eventWrapper = it
            showEvent(it)
        }

        val updateAddImage = Observer<Boolean> {
            if(it) {
                viewModel.getEventById(arguments?.getString(INTENT_DETAILS_ID_EXTRA)!!)
            }
        }

        viewModel.getEvent().observe(this, updateEvent)
        viewModel.getSuccesAddImage().observe(this,updateAddImage)

        viewModel.getEventById(arguments?.getString(INTENT_DETAILS_ID_EXTRA)!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_event_details, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.participant_number -> {

            }
            R.id.fragment_event_details_download_images -> {
                AlertDialog.Builder(context!!)
                    .setTitle(getString(R.string.download_all_images_title))
                    .setMessage(getString(R.string.download_all_images_message))
                    .setPositiveButton(getString(R.string.yes)) { dialog, which ->

                        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
                        val eventName = eventWrapper.event.title

                        for(image in galleryWrapper) {
                            val imageUrl = image.image.file!!.url
                            val fileName = image.image.file!!.name
                            ImageDownloader().downloadImage(imageUrl, fileName, dirPath, eventName, context!!)
                        }

                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.no)) { dialog, which ->
                        dialog.dismiss()


                    }
                    .create()
                    .show()
            }
            R.id.fragment_event_detail_fab_add_camera -> {
                val options = QuickPermissionsOptions()
                options.handleRationale = true
                options.rationaleMessage = "Nous avons vraiment besoin de ta caméra"
                options.permanentDeniedMethod = { permissionsPermanentlyDenied(it) }
                permisionCamera(options)
            }
            R.id.fragment_event_detail_fab_add_galery -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
            }
        }
    }

    private fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        // this will be called when some/all permissions required by the method are permanently
        // denied. Handle it your way.
        AlertDialog.Builder(context!!)
            .setTitle("Permission refusée")
            .setMessage(
                "This is the custom permissions permanently denied dialog. " +
                        "Please open app settings to open app settings for allowing permissions, " +
                        "or cancel to end the permission flow."
            )
            .setPositiveButton("App Settings") { dialog, which -> req.openAppSettings() }
            .setNegativeButton("Cancel") { dialog, which -> req.cancel() }
            .setCancelable(false)
            .show()
    }

    fun permisionCamera(options: QuickPermissionsOptions) =
        runWithPermissions(Manifest.permission.CAMERA, options = options) {
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_SELECT_IMAGE_IN_ALBUM -> {
                    val selectedImage = data!!.data
                    val logo = Bitmap.createScaledBitmap(
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedImage),
                        512,
                        384,
                        false
                    )
                    viewModel.addPicture(logo, eventWrapper.event)
                }
                REQUEST_TAKE_PHOTO -> {
                    if (data != null && data.extras != null) {
                        Picasso.get().isLoggingEnabled = true
                        val logo = data.extras.get("data") as Bitmap
                        viewModel.addPicture(logo,eventWrapper.event)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
            R.id.menu_event_details_share -> {
                navigator.displayShare(eventWrapper.event.objectId)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayImage(imageWrapper: ImageWrapper) {
        navigator.displayPhoto(imageWrapper.image.file!!.url, imageWrapper.image.objectId, eventWrapper.event.objectId, eventWrapper.event.title!!, imageWrapper.image.file!!.name)
    }

    private fun showEvent(eventWrapper: EventWrapper) {
        participant_number.text = eventWrapper.numberOfMembers()
        location_label.text = eventWrapper.event.location
        event_date_label.text = eventWrapper.dateCoverLabel()
        supervisor_label.text = eventWrapper.event.owner!!.username

        Picasso.get().load(eventWrapper.event.logo?.url).placeholder(R.mipmap.eventerize).into(event_detail_event_logo)

        event_detail_no_image_in_gallery_logo.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.mipmap.eventerize
            )
        )

        activity!!.title = eventWrapper.event.title

        //OnClickListener
        participant_number.setOnClickListener(this)
        fragment_event_details_download_images.setOnClickListener(this)
        fragment_event_detail_fab_add_camera.setOnClickListener(this)
        fragment_event_detail_fab_add_galery.setOnClickListener(this)

        val imageWrapperList: MutableList<ImageWrapper> = mutableListOf()

        if (eventWrapper.event.images != null) {
            eventWrapper.event.images!!.map {
                imageWrapperList.add(ImageWrapper(it))
            }
        }

        adapter.updateImageList(imageWrapperList)
        if (imageWrapperList.isNullOrEmpty()) {
            event_detail_no_image_in_gallery.visibility = View.VISIBLE
        } else {
            event_detail_no_image_in_gallery.visibility = View.INVISIBLE
        }
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
