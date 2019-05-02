package com.luna.eventerize.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator

class AuthActivity : AppCompatActivity() {

    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        navigator = Navigator(supportFragmentManager)

        if(savedInstanceState == null){
            navigator.displayLogin()
        }

    }
}