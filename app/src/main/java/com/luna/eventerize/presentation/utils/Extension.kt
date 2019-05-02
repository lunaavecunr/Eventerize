package com.luna.eventerize.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attchToRoot: Boolean = false): View {

    return LayoutInflater.from(context).inflate(layoutRes, this, attchToRoot)
}
