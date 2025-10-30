package com.xtrane.ui.parentsApp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xtrane.R
import com.xtrane.ui.commonApp.AccountFragment

import com.google.android.material.bottomnavigation.BottomNavigationMenuView

import android.util.Log
import android.widget.Toast
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.GetProfileController

import com.xtrane.utils.Constants
import com.xtrane.utils.FirebaseNotificationHelper
import com.xtrane.utils.FirebaseNotificationManager
import com.xtrane.utils.SharedPrefUserData


class ParentsMainActivity : AppCompatActivity() {
    var bottomNavigationViewTop: BottomNavigationView? = null
    private lateinit var notificationHelper: FirebaseNotificationHelper
    private lateinit var notificationManager: FirebaseNotificationManager

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_parent)

        supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            PHomeFragment(),
            "HomeFragment"
        ).commit()

        bottomNavigationViewTop = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigationViewTop!!.setOnNavigationItemSelectedListener(navListener)

        val menuView = bottomNavigationViewTop!!.getChildAt(0) as BottomNavigationMenuView

        Log.d(TAG, "onCreate: test>>" + menuView.childCount)

        if(intent.hasExtra("from") && intent.getStringExtra("from")!=null&& intent.getStringExtra("from")!="null"
            && intent.getStringExtra("from")!=""){
            GetProfileController(this,true,object:ControllerInterface{
                override fun onFail(error: String?) {

                }

                override fun <T> onSuccess(response: T, method: String) {

                }
            })
        }
        notificationHelper = FirebaseNotificationHelper(this)
        notificationManager = FirebaseNotificationManager(this)

        notificationHelper.getFirebaseToken { token ->
            if (token != null) {
                //binding.tvToken.text = "Token: ${token.take(20)}..."
                Log.e("Token=",token)
                Toast.makeText(this, "Token refreshed="+token, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to get token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val TAG = "ParentsMainActivity"
    override fun onBackPressed() {
        val myFragmentC: PHomeFragment? =
            supportFragmentManager.findFragmentByTag("HomeFragment") as PHomeFragment?
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
                    PHomeFragment()
                R.id.matchFragment -> selectedFragment =
                    MatchFragment()
                R.id.coachesFragment -> selectedFragment =
                    AllCoachesFragment()
                R.id.accountFragment -> {
                    Constants.userType=0
                    selectedFragment =
                        AccountFragment()
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
}