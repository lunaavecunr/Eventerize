package com.luna.eventerize.presentation.ui.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment<SignUpViewModel>(), View.OnClickListener {
    override val viewModelClass = SignUpViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        activity!!.title = getString(R.string.signup_title)

        fragment_signup_button_signup.setOnClickListener(this)

        val updateSuccess = Observer<Boolean> { postSuccess ->
            if(postSuccess){
                TODO()
            }
        }

        val updateError = Observer<EventerizeError> { postError ->
            if(postError != null){
                showError(activity!!, postError.message)
            }
        }

        viewModel.getSuccess().observe(this, updateSuccess)
        viewModel.getError().observe(this, updateError)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.fragment_signup_button_signup -> {
                viewModel.signup(fragment_signup_email.text.toString(), fragment_signup_username.text.toString(), fragment_signup_password.text.toString(), fragment_signup_confirm_password.text.toString(), fragment_signup_cgu.isChecked )
            }
        }
    }

    companion object {

        fun newInstance() = SignUpFragment()

    }
}
