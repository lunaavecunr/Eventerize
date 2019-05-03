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

    var areDateSame = MutableLiveData<Boolean>()

    /**
     * Return the [EventDate] [MutableLiveData]
     */
    fun getEventDate(eventType:String): MutableLiveData<EventDate> {
        if(eventType == BEGINDATE){
            return beginEvent
        }else{
            return endEvent
        }
    }

    fun updateAreDateSame(newValue:Boolean){
        areDateSame.postValue(newValue)
    }

    fun isEndBeforeBegin(beginDate: EventDate, endDate: EventDate) : Boolean{
        if(beginDate.year>=endDate.year){
            if(beginDate.month>=endDate.month){
                if(beginDate.day>=endDate.day){
                    return true
                }
            }
        }
        return false
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
    fun updateDate(eventType: String, data:EventDate){
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
     * Create an [EventDate] from three [Integer]
     */
    fun createDate(year:Int = 0, month:Int = 0, day:Int = 0):EventDate{
        return EventDate(year,month,day)
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