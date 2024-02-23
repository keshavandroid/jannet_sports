package com.e.jannet_stable_code.ui.coachApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.ui.commonApp.AccountFragment
import com.e.jannet_stable_code.utils.Constants


class CoachMainActivity : AppCompatActivity() {
    var bottomNavigationViewTop:BottomNavigationView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coach)

        supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            CHomeFragment(),
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
        }else{
            bottomNavigationViewTop!!.selectedItemId=R.id.homeFragment
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
                    Constants.userType=1
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