package com.xtrane.ui.coachApp

import android.app.ComponentCaller
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.xtrane.utils.FirebaseNotificationHelper
import com.xtrane.utils.FirebaseNotificationManager


class CoachMainActivity : AppCompatActivity() {
    var bottomNavigationViewTop: BottomNavigationView? = null
    lateinit var deviceRegisterController: IDeviceRegisterController
    var type: String = "";
    var message: String = "";
    var from: String = "";
    var eventId: String = "";
    private lateinit var notificationHelper: FirebaseNotificationHelper
    private lateinit var notificationManager: FirebaseNotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coach)

        if (intent.hasExtra("type")) {

            type= intent.getStringExtra("type").toString()
        }
        if (intent.hasExtra("from")) {

            from= intent.getStringExtra("from").toString()
        }
        if (intent.hasExtra("message")) {

            message= intent.getStringExtra("message").toString()
        }
        if (intent.hasExtra("title")) {

            title= intent.getStringExtra("title").toString()
        }
        if (intent.hasExtra("eventId")) {

            eventId= intent.getStringExtra("eventId").toString()
        }

        Log.e("CoachActivity1=", type.toString()+"="+from+"="+message);
        Log.e("CoachActivity2=", "="+from);
        Log.e("CoachActivity3=", "="+message);

        val CHomeFragment = CHomeFragment()
        val bundle = Bundle()
        bundle.putString("type", type)
        bundle.putString("message", message)
        bundle.putString("title", title.toString())
        bundle.putString("eventId", eventId.toString())
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
        notificationHelper = FirebaseNotificationHelper(this)
        notificationManager = FirebaseNotificationManager(this)

        notificationHelper.getFirebaseToken { token ->
            if (token != null) {
                //binding.tvToken.text = "Token: ${token.take(20)}..."
                Log.e("Token=", token)
                Toast.makeText(this, "Token refreshed=" + token, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show()
            }
        }
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