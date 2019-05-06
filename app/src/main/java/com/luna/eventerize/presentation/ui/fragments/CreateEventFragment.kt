package com.luna.eventerize.presentation.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
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
import androidx.core.content.FileProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.luna.eventerize.BuildConfig
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime

import java.util.*
/*

class CreateEventFragment : BaseFragment<CreateEventViewModel>(), View.OnClickListener {
}
   /* override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.manage_invitations_label -> {
                //Todo : Générer les invitations
            }
            R.id.begin_date_edit_text -> {
                initDatePicker(Calendar.getInstance(), CreateEventViewModel.BEGIN_DATE, begin_date_edit_text)
            }
            R.id.begin_hour_edit_text -> {
                initTimePicker(CreateEventViewModel.BEGIN_HOUR, begin_hour_edit_text)
            }
            R.id.end_day_edit_text -> {
                initDatePicker(Calendar.getInstance(), CreateEventViewModel.END_DATE, end_day_edit_text)
            }
            R.id.end_hour_edit_text -> {
                initTimePicker(CreateEventViewModel.END_HOUR, end_hour_edit_text)
            }
            R.id.event_creation_logo ->{
                val dialog = AlertDialog.Builder(context!!)
                    .setTitle(getString(R.string.logo_choice))
                    .setMessage(getString(R.string.logo_choice_message))
                    .setPositiveButton(getString(R.string.camera_chosen)
                    ) { dialog, id ->
                        takePhoto()
                    }
                    .setNegativeButton(getString(R.string.gallery_chosen)
                    ) { dialog, id ->
                        selectImageInAlbum()
                    }
                dialog.create()
                dialog.show()
            }
            R.id.validate_event ->{
            }
        }
    }

    private fun initLogo(code : Int){
        Picasso.get().load(code).transform(CircleTransform()).into(event_creation_logo)
    }

    private fun logoClickListener(){
        event_creation_logo.setOnClickListener(this)
    }

    private fun buttonListener(){
        validate_event.setOnClickListener(this)
    }

    override var viewModelClass = CreateEventViewModel::class
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item!!.itemId == android.R.id.home) {
            activity?.onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPickers()
        initToolbar()
        initMutableLiveData()
        generateInvitations()
        logoClickListener()
        buttonListener()
        initLogo(R.drawable.add_image)
    }

    private fun correctlyFilled():Boolean{
        return (isEditTextEmpty(event_title_input_layout)
                && isEditTextEmpty(event_location_layout)
                && isEditTextEmpty(begin_hour_text_input)
                && isEditTextEmpty(begin_date_text_input)
                && isEditTextEmpty(end_date_edit_text)
                && isEditTextEmpty(end_time_edit_text))
    }

    private fun isEditTextEmpty(editText: TextInputLayout):Boolean{
        return editText.editText!!.editableText.toString().isEmpty()
    }

    private fun setDateTextView(
        firstDate:Date,
        endDate: Date
    ) {
        val isSecondEmpty = end_day_edit_text.editableText.isNullOrEmpty()
        if(isSecondEmpty){
            updateEndDateEditText(firstDate,endDate)
        }else if(firstDate.after(viewModel.endEvent.value)){
            Toast.makeText(context,getString(R.string.end_date_before_begin_date),Toast.LENGTH_SHORT).show()
        }else{
            viewModel.updateDate(CreateEventViewModel.BEGIN_DATE,firstDate)
        }
    }

    private fun updateDate(date: Date, eventType:String){
        viewModel.updateDate(eventType,date)
    }

    /**
     * Init [DatePickerDialog] and [TimePickerDialog] pickers
     */
    private fun initPickers() {
        setTextEditOnClickListener()
    }

    private fun initMutableLiveData(){
        updateDateTextInputLayout(begin_date_edit_text,CreateEventViewModel.BEGIN_DATE)
        updateDateTextInputLayout(end_day_edit_text,CreateEventViewModel.END_DATE)
    }

    /**
     * Init [TextInputEditText] listeners
     */
    private fun setTextEditOnClickListener() {
        initEditTextListener(begin_date_edit_text)
        initEditTextListener(end_day_edit_text)
        initEditTextListener(begin_hour_edit_text)
        initEditTextListener(end_hour_edit_text)
    }

    private fun updateEndDateEditText(beginDate: Date, endDate: Date){
                if(!viewModel.isEndBeforeBegin(beginDate,endDate)){
                    Toast.makeText(activity,getString(R.string.end_date_before_begin_date),Toast.LENGTH_SHORT).show()
                }else{
                    updateDate(endDate,CreateEventViewModel.END_DATE)
                }
    }

    /**
     * Init a [TextInputEditText.setOnClickListener]
     */
    private fun initEditTextListener(editText: TextInputEditText) {
        editText.setOnClickListener(this)
    }


    /**
     * Init single [DatePickerDialog] for a [TextInputEditText]
     * @param eventDateType eventDataType for the [DatePickerDialog] ([CreateEventViewModel.BEGIN_DATE] or [CreateEventViewModel.END_DATE])
     * @param editText [TextInputEditText] which will receive data
     */
    private fun initDatePicker(now: Calendar, eventDateType: String, editText: TextInputEditText) {
        val dpd = DatePickerDialog.newInstance(
            { _, yearDatePicker, monthDatePicker, dayDatePicker ->
                if (eventDateType == CreateEventViewModel.BEGIN_DATE) {
                    setDateTextView(Date(yearDatePicker,monthDatePicker,dayDatePicker),Date(yearDatePicker,monthDatePicker,dayDatePicker))
                } else {
                    val currentDate = Date()
                    if(viewModel.beginEvent.value == null) currentDate else viewModel.beginEvent.value?.let {
                        updateEndDateEditText(
                            it,Date(yearDatePicker,monthDatePicker,dayDatePicker))
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

    /**
     * Set the date for the [TextInputEditText]
     */
    private fun updateDateTextInputLayout(editText: TextInputEditText, eventDate: String) {
        viewModel.getEventDate(eventDate).observe(this, androidx.lifecycle.Observer {
            editText.setText(
                "${viewModel.formatNumber(it.date)}/${viewModel.formatNumber(it.month+1)}/${viewModel.formatNumber(
                    it.year
                )}"
            )
        })
    }


    /**
     * Init single date picker event for a [TextInputEditText]
     * @param eventHourType type event for the time picker ([CreateEventViewModel.BEGIN_HOUR] or [CreateEventViewModel.END_HOUR])
     * @param editText edit text which will receive data
     */
    private fun initTimePicker(eventHourType: String, editText: TextInputEditText) {
        val htd = TimePickerDialog.newInstance(
            { _, hourDatePicker, minuteDatePicker, secondDatePicker ->
                viewModel.updateHour(
                    eventHourType,
                    viewModel.createTime(hourDatePicker, minuteDatePicker, secondDatePicker)
                )
            }, true
        )

        htd.version = TimePickerDialog.Version.VERSION_2
        htd.show(fragmentManager, "TimePickerDialog")
        updateHourTextInputLayout(editText, eventHourType)
    }

    /**
     * Set the date for the [TextInputEditText]
     */
    private fun updateHourTextInputLayout(editText: TextInputEditText, eventHour: String) {
        /*viewModel.getEventHour(eventHour).observe(this, androidx.lifecycle.Observer {
            editText.setText("${viewModel.formatNumber(it.hour)}:${viewModel.formatNumber(it.minutes)}")
        })*/
    }

    /**
     * Init the toolbar
     */
    private fun initToolbar() {
        navigator = Navigator(fragmentManager!!)
        initToolbarTitle()
        initToolbarBackNavigation()
    }

    /**
     * Init the toolbar title
     */
    private fun initToolbarTitle() {
        activity!!.title = getString(R.string.creating_event)
    }

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }
    fun takePhoto() {
        try {
           val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
           startActivityForResult(intent, REQUEST_TAKE_PHOTO)
       } catch (ex:IOException) {
           ex.printStackTrace()
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_SELECT_IMAGE_IN_ALBUM ->{
                    val selectedImage = data!!.data
                    Picasso.get().load(selectedImage).transform(CircleTransform()).into(event_creation_logo)
                }
                REQUEST_TAKE_PHOTO ->{
                    if (data != null && data.getExtras() != null) {
                        val imageBitmap = data.getExtras().get("data") as Bitmap
                        //viewModel.saveBitmap("Mon titre",imageBitmap)
                        event_creation_logo.setImageBitmap(imageBitmap)
                        //Picasso.get().load(selectedImage).transform(CircleTransform()).into(event_creation_logo)
                    }
                }
            }
        }
    }

private fun createImageFile():File{
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        val storageDir = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        //viewModel.imagePath = "file://" + image.getAbsolutePath();
        return image;
    }

    /**
     * Init the toolbar navigation
     */
    private fun initToolbarBackNavigation() {
        create_event_fragment_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(create_event_fragment_toolbar)
    }

    /**
     * Initialisation of the invitations generation
     */
    private fun generateInvitations() {
        manage_invitations_label.setOnClickListener(this)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateEventFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CreateEventFragment()
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }*/
}
*/