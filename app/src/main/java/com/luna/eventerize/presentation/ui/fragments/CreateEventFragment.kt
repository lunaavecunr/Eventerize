package com.luna.eventerize.presentation.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.material.textfield.TextInputEditText
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.createevent.CreateEventViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val BEGIN_DATE = "BEGIN_DATE"
const val END_DATE = "END_DATE"
const val BEGIN_HOUR = "BEGIN_HOUR"
const val END_HOUR = "END_HOUR"
const val REQUEST_TAKE_PHOTO = 100
const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
const val PERMISSION_CAMERA = 102

class CreateEventFragment : BaseFragment<CreateEventViewModel>(), View.OnClickListener {
    override val viewModelClass = CreateEventViewModel::class
    private lateinit var navigator: Navigator
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startHour: Date? = null
    private var endHour: Date? = null
    private var logo:Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SetListeners
        initListeners()

        event_creation_progress_bar.visibility = View.INVISIBLE

        //Toolbar
        navigator = Navigator(fragmentManager!!)
        activity!!.title = getString(R.string.creating_event)
        create_event_fragment_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(create_event_fragment_toolbar)

        //Logo
        val addImage = ContextCompat.getDrawable(context!!, R.drawable.ic_add_image)
        event_creation_logo.setImageDrawable(addImage)

        //Error managment
        val updateError = Observer<EventerizeError> {
            showError(context!!,it.message)
            validate_event.isEnabled = true
            event_creation_progress_bar.visibility = View.INVISIBLE
            validate_event.text = getString(R.string.generate_event)
            validate_event.setBackgroundResource(R.drawable.button_background)
        }

        val updateSuccessUpload = Observer<Boolean>{
            navigator.displayEventList()
        }

        viewModel.getError().observe(this, updateError)
        viewModel.getSuccess().observe(this, updateSuccessUpload)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item!!.itemId == android.R.id.home) {
            activity?.onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initListeners() {
        begin_date_edit_text.setOnClickListener(this)
        begin_hour_edit_text.setOnClickListener(this)
        end_day_edit_text.setOnClickListener(this)
        end_hour_edit_text.setOnClickListener(this)
        manage_invitations.setOnClickListener(this)
        validate_event.setOnClickListener(this)
        event_creation_logo.setOnClickListener(this)
    }

    private fun initDatePicker(editText: TextInputEditText, editTextType: String) {
        var now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            { _, yearDatePicker, monthDatePicker, dayDatePicker ->
                when (editTextType) {
                    BEGIN_DATE -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(yearDatePicker, monthDatePicker, dayDatePicker)
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        var date = Date(calendar.timeInMillis)
                        if (endDate != null) {
                            if (endDate!!.after(date)) {
                                updateTextEdit(date, editText, "dd/MM/yyyy")
                                startDate = date
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.end_date_before_begin_date),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            updateTextEdit(date, editText, "dd/MM/yyyy")
                            startDate = date
                        }

                    }
                    END_DATE -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(yearDatePicker, monthDatePicker, dayDatePicker)
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        var date = Date(calendar.timeInMillis)
                        if (startDate != null) {
                            if (startDate!!.before(date) || startDate!! == date) {
                                updateTextEdit(date, editText, "dd/MM/yyyy")
                                endDate = date
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.end_date_before_begin_date),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            updateTextEdit(date, editText, "dd/MM/yyyy")
                            endDate = date
                        }
                    }
                }
            },
            now.get(Calendar.YEAR), // Initial year selection
            now.get(Calendar.MONTH), // Initial month selection
            now.get(Calendar.DAY_OF_MONTH) // Inital day selection

        )
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(fragmentManager, "Datepickerdialog")
    }

    private fun initTimePicker(eventHourType: String, editText: TextInputEditText) {
        val htd = TimePickerDialog.newInstance(
            { _, hourDatePicker, minuteDatePicker, _ ->
                when (eventHourType) {
                    BEGIN_HOUR -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hourDatePicker)
                        calendar.set(Calendar.MINUTE, minuteDatePicker)
                        var date = Date(calendar.timeInMillis)
                        if((startDate != null && endDate!= null) && startDate!! == endDate){
                            if((endHour != null && date.before(endHour)) ||(endHour == null)){
                                updateTextEdit(date, editText,"HH:mm")
                                startHour = date
                            }else{
                                Toast.makeText(context,getString(R.string.end_hour_before_start_hour),Toast.LENGTH_SHORT).show()
                            }
                        } else  {
                            updateTextEdit(date, editText,"HH:mm")
                            startHour = date
                        }
                    }
                    END_HOUR -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hourDatePicker)
                        calendar.set(Calendar.MINUTE, minuteDatePicker)
                        var date = Date(calendar.timeInMillis)

                        if((startDate != null && endDate!= null) && startDate!! == endDate){
                            if((startHour != null && startHour!!.before(date)) || (startHour == null)){
                                updateTextEdit(date, editText,"HH:mm")
                                endHour = date
                            }else{
                                Toast.makeText(context,getString(R.string.end_hour_before_start_hour),Toast.LENGTH_SHORT).show()
                            }
                        } else  {
                            updateTextEdit(date, editText,"HH:mm")
                            endHour = date
                        }
                    }
                }
            }, true
        )

        htd.version = TimePickerDialog.Version.VERSION_2
        htd.show(fragmentManager, "TimePickerDialog")
    }

    private fun updateTextEdit(
        date: Date,
        editText: TextInputEditText,
        formatDate: String
    ) {
        val format = SimpleDateFormat(formatDate, Locale.FRENCH)
        var formattedDate = format.format(date)
        editText.setText(formattedDate)
    }

    private fun initPopup() {
        val dialog = AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.logo_choice))
            .setMessage(getString(R.string.logo_choice_message))
            .setPositiveButton(
                getString(R.string.camera_chosen)
            ) { _, _ ->
                takePhoto()
            }
            .setNegativeButton(
                getString(R.string.gallery_chosen)
            ) { _, _ ->
                selectImageInAlbum()
            }
        dialog.create()
        dialog.show()
    }

    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    private fun takePhoto() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_SELECT_IMAGE_IN_ALBUM -> {
                    val selectedImage = data!!.data
                    Picasso.get().load(selectedImage).centerCrop().resize(360,360).into(event_creation_logo)
                    logo = Bitmap.createScaledBitmap(
                        MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedImage),
                        512,
                        384,
                        false
                    )
                }
                REQUEST_TAKE_PHOTO -> {
                    if (data != null && data.extras != null) {
                        Picasso.get().isLoggingEnabled = true
                        logo = data.extras.get("data") as Bitmap
                        event_creation_logo.setImageBitmap(logo)
                    }
                }
            }
        }
    }

    private fun checkPermissions(requestCode: String, requestPermission: Int) {
        if(ContextCompat.checkSelfPermission(activity as Activity, requestCode)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(requestCode),
                requestPermission)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.begin_date_edit_text -> {
                initDatePicker(begin_date_edit_text, BEGIN_DATE)
            }
            R.id.end_day_edit_text -> {
                initDatePicker(end_day_edit_text, END_DATE)
            }
            R.id.begin_hour_edit_text -> {
                initTimePicker(BEGIN_HOUR, begin_hour_edit_text)
            }
            R.id.end_hour_edit_text -> {
                initTimePicker(END_HOUR, end_hour_edit_text)
            }
            R.id.event_creation_logo -> {
                //Permissions
                checkPermissions(Manifest.permission.CAMERA, PERMISSION_CAMERA)

                initPopup()
            }
            R.id.validate_event -> {
                /*validate_event.isEnabled = false
                event_creation_progress_bar.visibility = View.VISIBLE
                validate_event.text = getString(R.string.event_creation_ongoing_label)
                validate_event.setBackgroundResource(R.drawable.disabled_button_background)
                viewModel.saveEvent(event_title_input_layout.editText!!.text.toString(),event_location_layout.editText!!.text.toString(),startDate,startHour,endDate,endHour,logo)*/


                val url = "https://bigoud.games/eventerize/files/11558475925/5bdba112314c9ed811b76301c93d69c2_test.jpg"
                val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
                    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

                Toast.makeText(context, dirPath, Toast.LENGTH_SHORT).show()


                val fileName = "theo.jpg"



                var builder = NotificationCompat.Builder(activity!!, "notif")
                    .setContentTitle("Eventerize")
                    .setContentText("Image download")
                    .setSmallIcon(R.mipmap.eventerize)
                    .setPriority(NotificationCompat.PRIORITY_MAX)

                with(NotificationManagerCompat.from(context!!)) {
                    notify(0, builder.build())
                }










                val downloadId = PRDownloader.download(url, "$dirPath/Eventerize",fileName)
                    .build()
                    .setOnStartOrResumeListener {
                        onStart()
                    }
                    .setOnPauseListener {
                        onPause()
                    }
                    .setOnCancelListener {

                    }
                    .setOnProgressListener {progress ->
                        val PROGRESS_MAX = progress.totalBytes.toInt()

                        NotificationManagerCompat.from(context!!).apply {
                            // Issue the initial notification with zero progress
                            builder.setProgress(PROGRESS_MAX, progress.currentBytes.toInt(), false)
                            notify(0, builder.build())
                        }
                        //progressBar.setProgress(progress.currentBytes as Int, true)
                    }
                    .start(object : OnDownloadListener {
                        override fun onError(error: com.downloader.Error?) {
                            Toast.makeText(context, "Download error server : " + error!!.isServerError, Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, "Download error connect : " + error!!.isConnectionError, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDownloadComplete() {
                            NotificationManagerCompat.from(context!!).apply {
                                builder.setContentText("Download complete")
                                    .setProgress(0, 0, false)
                                notify(0, builder.build())
                            }


                            Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
                            val fileCreated = File("$dirPath/Eventerize/$fileName")


                            var arr = arrayOf(fileCreated.absolutePath)

                            var arr2 = arrayOf("images/*")
                            MediaScannerConnection.scanFile(context,arr, arr2) { s: String, uri: Uri ->

                            }


                        }
                    })

                //Picasso.get().load(url).into(getTarget("test"))
                /*val bm = Picasso.get().load(url).get()
                MediaStore.Images.Media.insertImage(activity!!.contentResolver, bm, fileName, "theo devant un soutif")*/


            }
        }
    }

    companion object {
        fun newInstance() = CreateEventFragment()
    }
}