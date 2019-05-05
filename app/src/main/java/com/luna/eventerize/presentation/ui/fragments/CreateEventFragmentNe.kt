package com.luna.eventerize.presentation.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
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
import java.lang.Exception
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
    private var photoFilePath:String? = null
    private var permissionsGranted: Boolean = false

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
        begin_date_edit_text.setOnClickListener(this)
        begin_hour_edit_text.setOnClickListener(this)
        end_day_edit_text.setOnClickListener(this)
        end_hour_edit_text.setOnClickListener(this)
        manage_invitations.setOnClickListener(this)
        validate_event.setOnClickListener(this)
        add_logo_floating_button.setOnClickListener(this)
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
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO)
    }


    private fun saveImage(bitmap: Bitmap){
        val root = Environment.getExternalStorageDirectory().toString()
        var folderTitle: String

        if(event_creation_title_text.editableText.toString().isNullOrBlank()){
            val date = Date()
            folderTitle = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(date)
        }else{
            folderTitle = event_creation_title_text.editableText.toString()
        }

        val myDir = File("$root/Eventerize/$folderTitle/")

        if(!myDir.exists()){
            myDir.mkdirs()
        }

        val fileName = "logo.jpg"
        val file = File(myDir,fileName)
        if(file.exists()){
            file.delete()
        }

        try{
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            photoFilePath = file.absolutePath
        }catch (e: Exception){
            Log.e("Error",e.localizedMessage)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_SELECT_IMAGE_IN_ALBUM -> {
                    val selectedImage = data!!.data
                    Picasso.get().load(selectedImage).centerCrop().resize(360,360).into(event_creation_logo)
                }
                REQUEST_TAKE_PHOTO -> {
                    if (data != null && data.extras != null) {
                        Picasso.get().isLoggingEnabled = true
                        val imageBitmap = data.extras.get("data") as Bitmap
                        saveImage(imageBitmap)
                        Picasso.get().load(photoFilePath!!.trim()).into(event_creation_logo)
                        //event_creation_logo.setImageBitmap(imageBitmap)

                    }
                }
            }
        }
    }

    private fun permissions(requestCode: String) {
        if(ContextCompat.checkSelfPermission(activity as Activity, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA)
        }
        if(ContextCompat.checkSelfPermission(activity as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_WRITE_STORAGE)
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
            R.id.add_logo_floating_button -> {
                initPopup()
            }
        }
    }

    companion object {
        fun newInstance() = CreateEventFragmentNe()

        private val REQUEST_TAKE_PHOTO = 100
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        private val PERMISSION_CAMERA = 102
        private val PERMISSION_WRITE_STORAGE = 2

    }
}