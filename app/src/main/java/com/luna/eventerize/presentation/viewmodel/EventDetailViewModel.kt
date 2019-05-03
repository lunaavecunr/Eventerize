package com.luna.eventerize.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.data.model.Event

class EventDetailViewModel: ViewModel() {
    var eventDetail = MutableLiveData<Event>()

    fun formatText(number: Int, text:String, plural:String):String{
        return if(number<10){
            "$number $text"
        }else{
            "$number $text$plural"
        }
    }


}