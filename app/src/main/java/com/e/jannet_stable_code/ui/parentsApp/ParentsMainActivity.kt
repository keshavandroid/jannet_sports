package com.e.jannet_stable_code.ui.parentsApp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.ui.commonApp.AccountFragment

import com.google.android.material.bottomnavigation.BottomNavigationMenuView

import kotlinx.android.synthetic.main.activity_main_parent.*
import android.util.Log
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.GetProfileController

import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData


class ParentsMainActivity : AppCompatActivity() {
    var bottomNavigationViewTop: BottomNavigationView? = null

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


        val menuView = bottom_navigatin_view.getChildAt(0) as BottomNavigationMenuView

        Log.d(TAG, "onCreate: test>>" + menuView.childCount)

        if(intent.hasExtra("from") && intent.getStringExtra("from")!=null&& intent.getStringExtra("from")!="null"&& intent.getStringExtra("from")!=""){
            GetProfileController(this,true,object:ControllerInterface{
                override fun onFail(error: String?) {

                }

                override fun <T> onSuccess(response: T, method: String) {

                }
            })
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