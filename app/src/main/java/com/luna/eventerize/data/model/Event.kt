package com.luna.eventerize.data.model

import android.graphics.drawable.Drawable

data class Event(val participantNumber:Int, val locationEvent:String, val beginEvent:String, val endingEvent: String, val supervisor:String, val logo:Drawable, val galleryList:ArrayList<String>, val title:String)