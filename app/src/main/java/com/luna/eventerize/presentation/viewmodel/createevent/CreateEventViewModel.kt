package com.luna.eventerize.presentation.viewmodel.createevent

import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luna.eventerize.data.model.EventDate
import com.luna.eventerize.data.model.EventHour
import java.util.*
import java.io.File
import java.io.FileOutputStream


class CreateEventViewModel : ViewModel(){

    /**
     * Return the [EventDate] [MutableLiveData]
     */
    /*fun getEventDate(eventType:String): MutableLiveData<Date> {
        if(eventType == BEGINDATE){
            return beginEvent
        }else{
            return endEvent
        }
    }

    fun saveBitmap(eventTitle:String, bitmap: Bitmap){
        val file_path = Environment.getExternalStorageDirectory().absolutePath + "/PhysicsSketchpad"
        val dir = File(file_path)
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dir, eventTitle)
        val fOut = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
    }

    /*fun isAllSet(title:String, location:String, beginDate:String, endDate:String, ){

    }*/

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
    }*/
}