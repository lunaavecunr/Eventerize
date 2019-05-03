package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.createevent.CreateEventViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_create_event.*

import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val EVENT_ID = "param1"

class CreateEventFragment : BaseFragment<CreateEventViewModel>(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.manage_invitations_label->{
                //Todo : Générer les invitations
            }
            R.id.begin_date_edit_text->{
                initDatePicker(Calendar.getInstance(), CreateEventViewModel.BEGINDATE, begin_date_edit_text)
            }
            R.id.begin_hour_edit_text->{
                initTimePicker(CreateEventViewModel.BEGINHOUR,begin_hour_edit_text)
            }
            R.id.end_day_edit_text->{
                initDatePicker(Calendar.getInstance(), CreateEventViewModel.ENDDATE, end_day_edit_text)
            }
            R.id.end_hour_edit_text->{
                initTimePicker(CreateEventViewModel.ENDHOUR,end_hour_edit_text)
            }
        }
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    override var viewModelClass = CreateEventViewModel::class
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(EVENT_ID)
        }
    }

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
        generateInvitations()
    }

    /**
     * Init [DatePickerDialog] and [TimePickerDialog] pickers
     */
    private fun initPickers() {
        setTextEditOnClickListener()
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

    /**
     * Init a [TextInputEditText.setOnClickListener]
     */
    private fun initEditTextListener(editText: TextInputEditText){
        editText.setOnClickListener(this)
    }


    /**
     * Init single [DatePickerDialog] for a [TextInputEditText]
     * @param eventDateType eventDataType for the [DatePickerDialog] ([CreateEventViewModel.BEGINDATE] or [CreateEventViewModel.ENDDATE])
     * @param editText [TextInputEditText] which will receive data
     */
    private fun initDatePicker(now: Calendar, eventDateType: String, editText: TextInputEditText) {
        val dpd = DatePickerDialog.newInstance(
            { _, yearDatePicker, monthDatePicker, dayDatePicker ->
                viewModel.updateDate(eventDateType,viewModel.createDate(yearDatePicker,monthDatePicker,dayDatePicker))
            },
            now.get(Calendar.YEAR), // Initial year selection
            now.get(Calendar.MONTH), // Initial month selection
            now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        )
        dpd.version = DatePickerDialog.Version.VERSION_2
        dpd.show(fragmentManager, "Datepickerdialog")
        updateDateTextInputLayout(editText,eventDateType)
    }

    /**
     * Set the date for the [TextInputEditText]
     */
    private fun updateDateTextInputLayout(editText: TextInputEditText, eventDate:String){
        viewModel.getEventDate(eventDate).observe(this,androidx.lifecycle.Observer {
            editText.setText("${viewModel.formatNumber(it.day)}/${viewModel.formatNumber(it.month)}/${viewModel.formatNumber(it.year)}")
        })
    }


    /**
     * Init single date picker event for a [TextInputEditText]
     * @param eventHourType type event for the time picker ([CreateEventViewModel.BEGINHOUR] or [CreateEventViewModel.ENDHOUR])
     * @param editText edit text which will receive data
     */
    private fun initTimePicker(eventHourType: String, editText: TextInputEditText) {
        val htd = TimePickerDialog.newInstance(
            { _, hourDatePicker, minuteDatePicker, secondDatePicker ->
                viewModel.updateHour(eventHourType,viewModel.createTime(hourDatePicker,minuteDatePicker,secondDatePicker))
            }, true
        )
        htd.version = TimePickerDialog.Version.VERSION_2
        htd.show(fragmentManager, "TimePickerDialog")
        updateHourTextInputLayout(editText,eventHourType)
    }

    /**
     * Set the date for the [TextInputEditText]
     */
    private fun updateHourTextInputLayout(editText: TextInputEditText, eventHour:String){
        viewModel.getEventHour(eventHour).observe(this,androidx.lifecycle.Observer {
            editText.setText("${viewModel.formatNumber(it.hour)}:${viewModel.formatNumber(it.minutes)}")
        })
    }

    /**
     * Init the toolbar
     */
    private fun initToolbar(){
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
    private fun generateInvitations(){
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
        fun newInstance(param1: String) =
            CreateEventFragment().apply {
                arguments = Bundle().apply {
                    putString(EVENT_ID, param1)
                }
            }
        @JvmStatic
        fun newInstance() = CreateEventFragment()
    }
}
