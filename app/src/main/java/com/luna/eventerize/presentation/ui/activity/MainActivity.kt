package com.luna.eventerize.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.luna.eventerize.EventerizeApp
import com.luna.eventerize.R

class MainActivity : AppCompatActivity() {
    private var repository = EventerizeApp.getInstance().repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository.login("dfghjkl", "test123")
    }
}
