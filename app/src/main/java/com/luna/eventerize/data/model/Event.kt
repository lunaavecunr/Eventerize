package com.luna.eventerize.data.model

import android.graphics.drawable.Drawable
import java.util.*
import kotlin.collections.ArrayList

data class Event(val participantNumber:Int, val locationEvent:String, val beginEvent:Date, val endingEvent: Date, val supervisor:String, val logo:Drawable, val galleryList:ArrayList<String>, val title:String)