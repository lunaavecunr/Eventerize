package com.luna.eventerize.data.model

import com.parse.ParseFile
import com.parse.ParseUser

class Image(imageParse: ImageParse) {
    var file: ParseFile? = null
    var user: ParseUser? = null
    var string: String? = null

    init {
        this.file = imageParse.file
        this.user = imageParse.user
        this.string = imageParse.getString()
    }
}