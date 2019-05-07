package com.luna.eventerize.presentation.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.LoginViewModel
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException


class LoginFragment : BaseFragment<LoginViewModel>(), View.OnClickListener {

    override val viewModelClass = LoginViewModel::class
    lateinit var navigator: Navigator
    private val SHARE_QR_CODE = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = getString(R.string.login_title)
        navigator = Navigator(fragmentManager!!)
        fragment_login_loginButton.setOnClickListener(this)
        fragment_login_createAccountTV.setOnClickListener(this)
        share_Button.setOnClickListener(this)

        val updateError = Observer<EventerizeError>{
            showError(activity!!, it.message)
        }

        val updateUser = Observer<ParseUser> {
            TODO()
        }

        viewModel.getError().observe(this, updateError)
        viewModel.getUser().observe(this, updateUser)

    }

    override fun onClick(v: View) {
       when(v.id) {
           R.id.fragment_login_loginButton -> {
                viewModel.login(fragment_login_emailField.text.toString(), fragment_login_passwordField.text.toString())
           }
           R.id.fragment_login_createAccountTV -> {
               navigator.displaySignUp()
           }
           R.id.share_Button -> {
               val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
               sharingIntent.type = "*/*"
               //val shareBodyText = "https://google.com"
               sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Je t'invites à mon event !")
               //sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
               //val qrCodeBitmap : Bitmap = null
               val bytes = ByteArrayOutputStream()
               //qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
               val f = File(Environment.getExternalStorageDirectory(),separator + "qrcode.jpg")
               try {
                   f.createNewFile()
                   val fo = FileOutputStream(f)
                   fo.write(bytes.toByteArray())
               } catch (e: IOException) {
                   e.printStackTrace()
               }
               sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/qrcode.jpg"))
               startActivityForResult(Intent.createChooser(sharingIntent, "Sharing Options"), SHARE_QR_CODE)

           }

       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SHARE_QR_CODE) {
            if(resultCode == Activity.RESULT_OK){
                val f = File(Environment.getExternalStorageDirectory(),separator + "qrcode.jpg")
                f.delete()
            }
        }

    }


}