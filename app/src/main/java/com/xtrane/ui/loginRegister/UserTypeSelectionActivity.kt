package com.xtrane.ui.loginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.xtrane.R
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.databinding.ActivityUserTypeSelectionBinding
import com.xtrane.ui.coachApp.CoachMainActivity
import com.xtrane.ui.loginRegister.loginScreen.LoginActivity
import com.xtrane.ui.loginRegister.addChildScreen.AddChildActivity
import com.xtrane.ui.parentsApp.ParentsMainActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData

class UserTypeSelectionActivity : AppCompatActivity() {
    private val TAG = "UserTypeSelectionActivi"
    private lateinit var binding: ActivityUserTypeSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_user_type_selection)
        binding = ActivityUserTypeSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        intent.extras?.keySet()?.forEach {
            Log.e(TAG,"USERACtivity_Key : $it Value : ${intent?.extras?.get(it).toString()}")
        }

        Log.d(TAG, "onCreate: test>>>" + isUserLoggedIn())

        val userType = SharedPrefUserData(this).getSavedData().usertype

        if(userType.equals("child",ignoreCase = true)){
            startActivity(
                Intent(this@UserTypeSelectionActivity, ParentsMainActivity::class.java)
                    .putExtra(
                        Constants.USER_TYPE,
                        intent.getStringExtra(Constants.USER_TYPE)
                    )
            )
            finish()
        }
        when {
            isUserLoggedIn() == 3 -> {
                val userType = SharedPrefUserData(this).getSavedData().usertype
                if (userType == "parent" || userType == "adult" || userType == "child") {
                    startActivity(
                        Intent(this@UserTypeSelectionActivity, ParentsMainActivity::class.java)
                    )
                    finish()
                } else if (userType == "coach") {
                    startActivity(
                        Intent(this@UserTypeSelectionActivity, CoachMainActivity::class.java)
                    )
                    finish()
                }
            }
            isUserLoggedIn() == 2 -> {
                val userType = SharedPrefUserData(this).getSavedData().usertype

                when (userType) {
                    "parent" -> {
                        startActivity(
                            Intent(this@UserTypeSelectionActivity, AddChildActivity::class.java)
                                .putExtra(
                                    Constants.USER_TYPE,
                                    intent.getStringExtra(Constants.USER_TYPE)
                                )
                        )
                        finish()
                    }
                    "child" -> {
                        startActivity(
                            Intent(this@UserTypeSelectionActivity, ParentsMainActivity::class.java)
                                .putExtra(
                                    Constants.USER_TYPE,
                                    intent.getStringExtra(Constants.USER_TYPE)
                                )
                        )
                        finish()
                    }
                    "adult" -> {
                        startActivity(
                            Intent(this@UserTypeSelectionActivity, ParentsMainActivity::class.java)
                                .putExtra(
                                    Constants.USER_TYPE,
                                    intent.getStringExtra(Constants.USER_TYPE)
                                )
                        )
                        finish()
                    }
                    "coach" -> {
                        startActivity(
                            Intent(this@UserTypeSelectionActivity, SelectSportsActivity::class.java)
                                .putExtra(
                                    Constants.USER_TYPE,
                                    intent.getStringExtra(Constants.USER_TYPE)
                                )
                        )
                        finish()
                    }
                }
            }
            isUserLoggedIn() == 1 -> {
                startActivity(
                    Intent(
                        this@UserTypeSelectionActivity,
                        PhoneEmailVerificationActivity::class.java
                    ).putExtra(Constants.CHILD_FROM, Constants.EMAIL_VERIFICATION)
                )
            }

        }
        initiateUI()
    }

    private fun initiateUI() {
        binding.txtParticipant.setOnClickListener {
            startActivity(
                Intent(
                    this@UserTypeSelectionActivity,
                    LoginActivity::class.java
                ).putExtra(
                    Constants.USER_TYPE, Constants.PARTICIPANT
                )
            )
        }
        binding.txtCoach.setOnClickListener {
            startActivity(
                Intent(
                    this@UserTypeSelectionActivity,
                    LoginActivity::class.java
                ).putExtra(
                    Constants.USER_TYPE, Constants.COACH
                )
            )
        }

        binding.txtChild.setOnClickListener {
            startActivity(
                    Intent(
                            this@UserTypeSelectionActivity,
                            LoginActivity::class.java
                    ).putExtra(
                            Constants.USER_TYPE, Constants.CHILD
                    )
            )
        }

        binding.txtAdult.setOnClickListener {
            startActivity(
                Intent(
                    this@UserTypeSelectionActivity,
                    LoginActivity::class.java
                ).putExtra(
                    Constants.USER_TYPE, Constants.ADULT
                )
            )
        }

        val storeData = StoreUserData(this)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val data = storeData.getString(Constants.COACH_ID)
        if (data!=""&&token!=""){

            startActivity(
                Intent(this@UserTypeSelectionActivity, CoachMainActivity::class.java)
            )
            finish()


        }
    }

    private fun isUserLoggedIn(): Int {
        try {
            val data = SharedPrefUserData(this).getSavedData()
            return if (data.token != "" && data.registerStep == "1") 1
            else if (data.token != "" && data.registerStep == "2") 2
            else if (data.token != "" && data.registerStep == "3") 3
            else 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}