package com.luna.eventerize.presentation.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.ui.picasso.CircleTransform
import com.luna.eventerize.presentation.viewmodel.createevent.CreateEventViewModel
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val BEGINDATE = "BEGINDATE"
const val ENDDATE = "ENDDATE"
const val BEGINHOUR = "BEGINHOUR"
const val ENDHOUR = "ENDHOUR"

class CreateEventFragmentNe : BaseFragment<CreateEventViewModel>(), View.OnClickListener {
    override val viewModelClass = CreateEventViewModel::class
    private lateinit var navigator: Navigator
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var startHour: Date? = null
    private var endHour: Date? = null
    private var isFormCorrectlyFilled: Boolean = false
    private var photoFilePath:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SetListeners
        initListeners()

        //Toolbar
        navigator = Navigator(fragmentManager!!)
        activity!!.title = getString(R.string.creating_event)
        create_event_fragment_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(create_event_fragment_toolbar)

        //Logo
        Picasso.get().load(R.drawable.add_image).into(event_creation_logo)
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
        event_creation_logo.setOnClickListener(this)
        begin_date_edit_text.setOnClickListener(this)
        begin_hour_edit_text.setOnClickListener(this)
        end_day_edit_text.setOnClickListener(this)
        end_hour_edit_text.setOnClickListener(this)
        manage_invitations.setOnClickListener(this)
        validate_event.setOnClickListener(this)
    }

    private fun checkIfFormIsCorrectlyFilled(): Boolean {
        if (event_creation_title_text.editableText.toString().isNullOrBlank()) {
            displayErrorMessage(getString(R.string.no_title_event))
            return false
        }
        if (startDate == null) {
            displayErrorMessage(getString(R.string.no_start_date))
            return false
        }
        if (startHour == null) {
            displayErrorMessage(getString(R.string.no_start_hour))
            return false
        }
        if (endDate == null) {
            displayErrorMessage(getString(R.string.no_end_date))
            return false
        }
        if (endHour == null) {
            displayErrorMessage(getString(R.string.no_end_hour))
            return false
        }
        return true
    }

    private fun displayErrorMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initDatePicker(editText: TextInputEditText, editTextType: String) {
        var now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            { _, yearDatePicker, monthDatePicker, dayDatePicker ->
                when (editTextType) {
                    BEGINDATE -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(yearDatePicker, monthDatePicker, dayDatePicker)
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
                    ENDDATE -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(yearDatePicker, monthDatePicker, dayDatePicker)
                        var date = Date(calendar.timeInMillis)
                        if (startDate != null) {
                            if (startDate!!.before(date)) {
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
                    BEGINHOUR -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hourDatePicker)
                        calendar.set(Calendar.MINUTE, minuteDatePicker)
                        var date = Date(calendar.timeInMillis)
                        updateTextEdit(date, editText, "HH:mm")
                        startHour = date
                    }
                    ENDHOUR -> {
//                        updateTextEdit(date, editText,"HH:mm")
//                        startDate = date
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
            ) { dialog, id ->
                takePhoto()
            }
            .setNegativeButton(
                getString(R.string.gallery_chosen)
            ) { dialog, id ->
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
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "${timeStamp}_logo", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            photoFilePath = absolutePath
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_SELECT_IMAGE_IN_ALBUM -> {
                    val selectedImage = data!!.data
                    Picasso.get().load(selectedImage).transform(CircleTransform()).into(event_creation_logo)
                }
                REQUEST_TAKE_PHOTO -> {
                    if (data?.extras != null) {
                        val imageBitmap = data.extras.get("data") as Bitmap

                        Picasso.get().load(photoFilePath).transform(CircleTransform()).into(event_creation_logo)
                    }
                }
            }
        }
    }

    private fun permissions(){
        if (ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity as AppCompatActivity,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(activity as AppCompatActivity,
                    arrayOf(Manifest.permission.READ_CONTACTS))

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.begin_date_edit_text -> {
                initDatePicker(begin_date_edit_text, BEGINDATE)
                isFormCorrectlyFilled = checkIfFormIsCorrectlyFilled()
            }
            R.id.end_day_edit_text -> {
                initDatePicker(end_day_edit_text, ENDDATE)
                isFormCorrectlyFilled = checkIfFormIsCorrectlyFilled()
            }
            R.id.begin_hour_edit_text -> {
                initTimePicker(BEGINHOUR, begin_hour_edit_text)
                isFormCorrectlyFilled = checkIfFormIsCorrectlyFilled()
            }
            R.id.end_hour_edit_text -> {
                initTimePicker(ENDHOUR, end_hour_edit_text)
                isFormCorrectlyFilled = checkIfFormIsCorrectlyFilled()
            }
            R.id.event_creation_logo -> {
                initPopup()
            }
        }
    }

    companion object {
        fun newInstance() = CreateEventFragmentNe()

        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

    }
}