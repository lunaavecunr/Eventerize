package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.createevent.CreateEventViewModel
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_create_event.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

const val BEGINDATE = "BEGINDATE"
const val ENDDATE = "ENDDATE"
const val BEGINHOUR = "BEGINHOUR"
const val ENDHOUR = "ENDHOUR"

class CreateEventFragmentNe : BaseFragment<CreateEventViewModel>(), View.OnClickListener {
    override val viewModelClass = CreateEventViewModel::class
    private lateinit var navigator:Navigator
    private var startDate:Date? = null
    private var endDate:Date? = null
    private var startHour:Date? = null
    private var endHour:Date? = null

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

    private fun initDatePicker(editText: TextInputEditText, editTextType:String) {
        var now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            { _, yearDatePicker, monthDatePicker, dayDatePicker ->
                when(editTextType){
                    BEGINDATE->{
                        val calendar = Calendar.getInstance()
                        calendar.set(yearDatePicker, monthDatePicker, dayDatePicker)
                        var date = Date(calendar.timeInMillis)
                        if(endDate != null){
                            if(endDate!!.after(date)){
                                updateTextEdit(date, editText,"dd/MM/yyyy")
                                startDate = date
                            }else{
                                Toast.makeText(context,getString(R.string.end_date_before_begin_date),Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            updateTextEdit(date, editText,"dd/MM/yyyy")
                            startDate = date
                        }

                    }
                    ENDDATE->{
                        val calendar = Calendar.getInstance()
                        calendar.set(yearDatePicker, monthDatePicker, dayDatePicker)
                        var date = Date(calendar.timeInMillis)
                        if(startDate != null){
                            if(startDate!!.before(date)){
                                updateTextEdit(date, editText,"dd/MM/yyyy")
                                endDate = date
                            }else{
                                Toast.makeText(context,getString(R.string.end_date_before_begin_date),Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            updateTextEdit(date, editText,"dd/MM/yyyy")
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
                when(eventHourType){
                    BEGINHOUR ->{
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, hourDatePicker)
                        calendar.set(Calendar.MINUTE, minuteDatePicker)
                        var date = Date(calendar.timeInMillis)
                        updateTextEdit(date, editText, "HH:mm")
                        startHour = date
                    }
                    ENDHOUR ->{
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

    override fun onClick(v: View){
        when (v.id){
            R.id.begin_date_edit_text -> {
                initDatePicker(begin_date_edit_text,BEGINDATE)
            }
            R.id.end_day_edit_text -> {
                initDatePicker(end_day_edit_text, ENDDATE)
            }
            R.id.begin_hour_edit_text ->{
                initTimePicker(BEGINHOUR,begin_hour_edit_text)
            }
        }
    }
    companion object {
        fun newInstance() = CreateEventFragmentNe()

        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

    }
}