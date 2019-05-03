package com.luna.eventerize.presentation.viewmodel.createevent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.data.model.EventDate
import com.luna.eventerize.data.model.EventHour
import java.util.*

class CreateEventViewModel : ViewModel(){
    //Mutable Live Data
    var beginEvent = MutableLiveData<Date>()
    var endEvent = MutableLiveData<Date>()
    var beginEventHour = MutableLiveData<EventHour>()
    var endEventHour = MutableLiveData<EventHour>()
    var areDateSame = MutableLiveData<Boolean>()

    /**
     * Return the [EventDate] [MutableLiveData]
     */
    fun getEventDate(eventType:String): MutableLiveData<Date> {
        if(eventType == BEGINDATE){
            return beginEvent
        }else{
            return endEvent
        }
    }

    fun updateAreDateSame(newValue:Boolean){
        areDateSame.postValue(newValue)
    }

    fun isEndBeforeBegin(beginDate: Date, endDate: Date) : Boolean{
        if(beginDate != endDate){
            if(endDate.before(beginDate)){
                return false
            }
        }
        return true
    }

    /**
     * Format an [Integer] to have two digits
     */
    fun formatNumber(number:Int):String{
        if(number<10){
            return "0$number"
        }else{
            return "$number"
        }
    }

    /**
     * Post value of an [EventDate] [MutableLiveData]
     */
    fun updateDate(eventType: String, data:Date){
        getEventDate(eventType).postValue(data)
    }


    /**
     * Post value of an [EventDate] [MutableLiveData]
     */
    fun updateHour(eventType: String, data:EventHour){
        getEventHour(eventType).postValue(data)
    }


    /**
     * Return the [EventHour] [MutableLiveData]
     */
    fun getEventHour(eventType:String): MutableLiveData<EventHour> {
        if(eventType == BEGINHOUR){
            return beginEventHour
        }else{
            return endEventHour
        }
    }


    /**
     * Create an [EventHour] from three [Integer]
     */
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