package com.luna.eventerize.data.model

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseRelation
import com.parse.ParseUser

@ParseClassName("Image")
class Image(var file: String, var user: ParseUser): ParseObject()