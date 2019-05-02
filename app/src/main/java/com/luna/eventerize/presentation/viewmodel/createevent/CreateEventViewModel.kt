package com.luna.eventerize.presentation.viewmodel.createevent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.data.model.EventDate
import com.luna.eventerize.data.model.EventHour

class CreateEventViewModel : ViewModel(){
    //Mutable Live Data
    var beginEvent = MutableLiveData<EventDate>()
    var endEvent = MutableLiveData<EventDate>()
    var beginEventHour = MutableLiveData<EventHour>()
    var endEventHour = MutableLiveData<EventHour>()

    fun getEventDate(eventType:String): MutableLiveData<EventDate> {
        if(eventType == BEGINDATE){
            return beginEvent
        }else{
            return endEvent
        }
    }

    fun updateDate(eventType: String, data:EventDate){
        getEventDate(eventType).postValue(data)
    }

    fun updateHour(eventType: String, data:EventHour){
        getEventHour(eventType).postValue(data)
    }

    fun getEventHour(eventType:String): MutableLiveData<EventHour> {
        if(eventType == BEGINHOUR){
            return beginEventHour
        }else{
            return endEventHour
        }
    }

    fun createDate(year:Int = 0, month:Int = 0, day:Int = 0):EventDate{
        return EventDate(year,month,day)
    }

    fun createTime(hour:Int = 0, minutes:Int = 0, seconds:Int = 0):EventHour{
        return EventHour(hour,minutes,seconds)
    }

    companion object{
        const val BEGINDATE = "BEGINDATE"
        const val ENDDATE = "ENDDATE"
        const val BEGINHOUR = "BEGINHOUR"
        const val ENDHOUR = "ENDHOUR"
    }
}