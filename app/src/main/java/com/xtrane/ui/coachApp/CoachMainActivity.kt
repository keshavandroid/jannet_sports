package com.xtrane.ui.coachApp

import android.app.ComponentCaller
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.xtrane.R
import com.xtrane.retrofit.controller.DeviceRegisterController
import com.xtrane.retrofit.controller.IDeviceRegisterController
import com.xtrane.ui.commonApp.AccountFragment
import com.xtrane.ui.parentsApp.PHomeFragment
import com.xtrane.utils.Constants


class CoachMainActivity : AppCompatActivity() {
    var bottomNavigationViewTop: BottomNavigationView? = null
    lateinit var deviceRegisterController: IDeviceRegisterController
    var type: String = "";
    var message: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coach)

//        if (intent.hasExtra("type")) {
//
//
//        }

        val type = intent.getStringExtra("type")
        val from = intent.getStringExtra("from")
        val message = intent.getStringExtra("message")


        Log.e("CoachActivity=", type.toString())



        val CHomeFragment = CHomeFragment()
        val bundle = Bundle()
        bundle.putString("type", type)
        bundle.putString("message", message)
        CHomeFragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            CHomeFragment,
            "HomeFragment"
        ).commit()

        bottomNavigationViewTop = findViewById(R.id.bottom_navigatin_view)
        bottomNavigationViewTop!!.setOnNavigationItemSelectedListener(navListener)

        /*   val item=MenuItem()

           navListener.onNavigationItemSelected(R.id.homeFragment)*/

    }


    override fun onBackPressed() {
        val myFragmentC: CHomeFragment? =
            supportFragmentManager.findFragmentByTag("HomeFragment") as CHomeFragment?
        if (myFragmentC != null && myFragmentC.isVisible) {
            finishAffinity()
        } else {
            bottomNavigationViewTop!!.selectedItemId = R.id.homeFragment
        }
    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.homeFragment -> selectedFragment =
                    CHomeFragment()

                R.id.teamFragment -> selectedFragment =
                    CTeamFragment()

                R.id.accountFragment -> {
                    selectedFragment =
                        AccountFragment()
                    Constants.userType = 1
                }
            }
            if (item.itemId == R.id.homeFragment) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.nav_fragment,
                    selectedFragment!!,
                    "HomeFragment"
                ).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(
                    R.id.nav_fragment,
                    selectedFragment!!
                ).commit()
            }

            true
        }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        setIntent(intent) // <- very important

        val type = intent?.getStringExtra("type")
        val from = intent?.getStringExtra("from")
        val message = intent?.getStringExtra("message")

        Log.e("FCM Debug NewIntent", "From: $from, Type: $type, Message: $message")
    }
}