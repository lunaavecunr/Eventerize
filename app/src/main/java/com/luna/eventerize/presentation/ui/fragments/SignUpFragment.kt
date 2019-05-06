package com.luna.eventerize.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.luna.eventerize.R
import com.luna.eventerize.data.model.EventerizeError
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.utils.showError
import com.luna.eventerize.presentation.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment<SignUpViewModel>(), View.OnClickListener {
    override val viewModelClass = SignUpViewModel::class
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)
        navigator = Navigator(fragmentManager!!)

        activity!!.title = getString(R.string.signup_title)
        fragment_signup_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(fragment_signup_toolbar)

        fragment_signup_button_signup.setOnClickListener(this)
        fragment_signup_logout.setOnClickListener(this)

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

        //TEMP
        val logout = Observer<Boolean> { postSuccess ->
            navigator.displayLogin()
        }

        viewModel.getSuccess().observe(this, updateSuccess)
        viewModel.getError().observe(this, updateError)

        //TEMP
        viewModel.getSucess2().observe(this, logout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.fragment_signup_button_signup -> {
                viewModel.signUp(fragment_signup_email.text.toString(), fragment_signup_username.text.toString(), fragment_signup_password.text.toString(), fragment_signup_confirm_password.text.toString(), fragment_signup_cgu.isChecked )
            }
            //TEMP
            R.id.fragment_signup_logout -> {
                viewModel.logout()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {

        fun newInstance() = SignUpFragment()

    }
}
