package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
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


class LoginFragment : BaseFragment<LoginViewModel>(), View.OnClickListener {

    override val viewModelClass = LoginViewModel::class
    lateinit var navigator: Navigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = getString(R.string.login_title)
        navigator = Navigator(fragmentManager!!)
        if (ParseUser.getCurrentUser() != null) {
            navigator.displayEventList()
        }
        fragment_login_loginButton.setOnClickListener(this)
        fragment_login_createAccountTV.setOnClickListener(this)

        val updateError = Observer<EventerizeError>{
            showError(activity!!, it.message)
        }

        val updateUser = Observer<ParseUser> {
            navigator.displayEventList()
        }
        viewModel.getError().observe(this, updateError)
        viewModel.getUser().observe(this, updateUser)

//        val bitMatrix: BitMatrix
//        try {
//            bitMatrix = MultiFormatWriter().encode(
//                "CeciEstUnTest",
//                BarcodeFormat.QR_CODE,
//                500, 500, null
//            )
//
//        } catch (illegalArgumentException: IllegalArgumentException) {
//            TODO()
//        }
//
//        val bitMatrixWidth = bitMatrix.width
//
//        val bitMatrixHeight = bitMatrix.height
//
//        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
//
//        for (y in 0 until bitMatrixHeight) {
//            val offset = y * bitMatrixWidth
//
//            for (x in 0 until bitMatrixWidth) {
//
//                pixels[offset + x] = if (bitMatrix.get(x, y))
//                    resources.getColor(R.color.black)
//                else
//                    resources.getColor(R.color.white)
//            }
//        }
//        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
//
//        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
//        qrcode.setImageBitmap(bitmap)

    }

    override fun onClick(v: View) {
       when(v.id) {
           R.id.fragment_login_loginButton -> {
                viewModel.login(fragment_login_emailField.text.toString(), fragment_login_passwordField.text.toString())
            }
            R.id.fragment_login_createAccountTV -> {
                navigator.displaySignUp()
            }

       }
    }


}