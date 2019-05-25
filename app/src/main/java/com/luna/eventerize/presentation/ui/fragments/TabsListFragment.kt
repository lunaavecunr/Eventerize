package com.luna.eventerize.presentation.ui.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import com.luna.eventerize.R
import com.luna.eventerize.presentation.navigator.Navigator
import com.luna.eventerize.presentation.ui.fragments.base.BaseFragment
import com.luna.eventerize.presentation.viewmodel.TabsListViewModel
import kotlinx.android.synthetic.main.fragment_tabs_list.*
import com.google.android.material.tabs.TabLayout
import com.luna.eventerize.data.model.EventListKey


class TabsListFragment : BaseFragment<TabsListViewModel>(), View.OnClickListener {
    private var lastTabs: EventListKey? = null
    private lateinit var navigator: Navigator
    override val viewModelClass = TabsListViewModel::class


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onActivityCreated(savedInstanceState)

        activity!!.title = getString(R.string.fragment_list_event_title)
        (activity as AppCompatActivity).setSupportActionBar(fragment_tabs_list_toolbar)

        fragment_event_list_fab_create_event.setOnClickListener(this)
        fragment_event_list_fab_qr_code.setOnClickListener(this)

        navigator = Navigator(fragmentManager!!)

        if (lastTabs != null){
            Log.e("TAG", lastTabs.toString())
            if(fragment_tabs_list_tabs.getTabAt(lastTabs!!.key) != null) {
                fragment_tabs_list_tabs.getTabAt(lastTabs!!.key)!!.select()
                navigator.displayEventWithBackStack(lastTabs!!.key)
            }
        } else {
            navigator.displayEventWithBackStack(0)
        }

        fragment_tabs_list_tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        lastTabs = EventListKey.ALL
                        navigator.displayEventWithBackStack(0)
                    }
                    1 -> {
                        lastTabs = EventListKey.ORGANIZER
                        navigator.displayEventWithBackStack(1)

                    }
                    2 -> {
                        lastTabs = EventListKey.MEMBER
                        navigator.displayEventWithBackStack(2)

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tabs_list, container, false)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.fragment_event_list_fab_qr_code -> {
                val options = QuickPermissionsOptions()
                options.handleRationale = true
                options.rationaleMessage = "Nous avons vraiment besoin de ta caméra"
                options.permanentDeniedMethod = { permissionsPermanentlyDenied(it) }
                startQRScanner(options)
            }
            R.id.fragment_event_list_fab_create_event -> {
                navigator.displayEventCreation()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(activity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Log.d("QRCODE", result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        // this will be called when some/all permissions required by the method are permanently
        // denied. Handle it your way.
        AlertDialog.Builder(context!!)
            .setTitle("Permission refusée")
            .setMessage(
                "This is the custom permissions permanently denied dialog. " +
                        "Please open app settings to open app settings for allowing permissions, " +
                        "or cancel to end the permission flow."
            )
            .setPositiveButton("App Settings") { dialog, which -> req.openAppSettings() }
            .setNegativeButton("Cancel") { dialog, which -> req.cancel() }
            .setCancelable(false)
            .show()
    }

    fun startQRScanner(options: QuickPermissionsOptions) =
        runWithPermissions(Manifest.permission.CAMERA, options = options) {
            IntentIntegrator(activity).initiateScan()
        }

    companion object {
        fun newInstance(): TabsListFragment = TabsListFragment()
    }
}