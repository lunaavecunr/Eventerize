package com.luna.eventerize.presentation.ui.fragments.createEvent

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.luna.eventerize.R
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.createevent.CreateEventViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_create_event.*

import java.util.*





// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateEventFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateEventFragment : BaseFragment<CreateEventViewModel>(), View.OnClickListener {
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    override var viewModelClass = CreateEventViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPickers()
    }

    private fun initPickers() {
        setDateEvents()
        setHourEvents()
    }

    private fun setDateEvents() {
        loadDateTimePicker(begin_date_edit_text,CreateEventViewModel.BEGINDATE)
        loadDateTimePicker(end_day_edit_text,CreateEventViewModel.ENDDATE)
    }

    private fun setHourEvents() {
        loadDateTimePicker(begin_hour_edit_text,CreateEventViewModel.BEGINHOUR)
        loadDateTimePicker(end_hour_edit_text,CreateEventViewModel.ENDHOUR)
    }

    private fun loadDateTimePicker(editText: TextInputEditText, eventType: String){
            editText.setOnClickListener {
                if(eventType == CreateEventViewModel.BEGINDATE || eventType == CreateEventViewModel.ENDDATE){
                    initDatePicker(Calendar.getInstance(), eventType, editText)
                }else {
                    initTimePicker(eventType, editText)
                }
            }
    }

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

    private fun updateDateTextInputLayout(editText: TextInputEditText, eventDate:String){
        viewModel.getEventDate(eventDate).observe(this,androidx.lifecycle.Observer {
            editText.setText("${it.day}/${it.month}/${it.year}")
        })
    }

    private fun updateHourTextInputLayout(editText: TextInputEditText, eventHour:String){
        viewModel.getEventHour(eventHour).observe(this,androidx.lifecycle.Observer {
            editText.setText("${it.hour}:${it.minutes}")
        })
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
        fun newInstance(param1: String, param2: String) =
            CreateEventFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
