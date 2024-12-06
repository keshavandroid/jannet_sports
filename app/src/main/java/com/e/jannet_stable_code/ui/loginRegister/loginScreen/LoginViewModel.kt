package com.e.jannet_stable_code.ui.loginRegister.loginScreen

import android.content.Intent
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.LoginController
import com.e.jannet_stable_code.retrofit.response.LoginResponse
import com.e.jannet_stable_code.ui.coachApp.CoachMainActivity
import com.e.jannet_stable_code.ui.parentsApp.ParentsMainActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.Utilities

class LoginViewModel(val activity: LoginActivity) {
    var email = ""
    var pwd = ""
    fun dataValid(email: String, pwd: String): Boolean {

        if (email.trim() == "") {
            Utilities.showToast(activity, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail(email.trim())) {
            Utilities.showToast(activity, "Please enter a valid email to continue.")
            return false
        } else if (pwd.trim() == "") {
            Utilities.showToast(activity, "Please enter password to continue.")
            return false
        } else if (pwd.trim().length < 6) {
            Utilities.showToast(activity, "Please enter a longer password to continue.")
            return false
        }
        this.email = email;
        this.pwd = pwd;
        return true
    }

    fun proceedLogin(userType: String) {
        LoginController(
            context = activity,
            email = email,
            pwd = pwd,
            userType= userType,
            controllerInterface = object : ControllerInterface {
                override fun onFail(error: String?) {
                    Utilities.showToast(activity, error)
                }

                override fun <T> onSuccess(response: T, method: String) {
                    if (method == "LoginResp") {
                        try {
                            val data = response as LoginResponse
                            SharedPrefUserData(activity).saveLoginResp(data.getResult()!!)

//                            if (activity.intent.getStringExtra(Constants.USER_TYPE).equals(Constants.COACH)) {
//                                activity.startActivity(
//                                    Intent(activity, CoachMainActivity::class.java)
//                                        .putExtra(Constants.USER_TYPE, activity.intent.getStringExtra(Constants.USER_TYPE)))
//                                activity.finish()
//                            } else
//
                            if (activity.intent.getStringExtra(Constants.USER_TYPE).equals(Constants.PARTICIPANT)) {
                                activity.startActivity(
                                    Intent(activity, ParentsMainActivity::class.java)
                                        .putExtra(
                                            Constants.USER_TYPE,
                                            activity.intent.getStringExtra(Constants.USER_TYPE)
                                        )
                                )
                                activity.finish()
                            }else if(activity.intent.getStringExtra(Constants.USER_TYPE).equals(Constants.CHILD)){
                                activity.startActivity(
                                        Intent(activity, ParentsMainActivity::class.java)
                                                .putExtra(
                                                        Constants.USER_TYPE,
                                                        activity.intent.getStringExtra(Constants.USER_TYPE)
                                                )
                                )
                                activity.finish()
                            }else if (activity.intent.getStringExtra(Constants.USER_TYPE).equals(Constants.ADULT)) {
                                activity.startActivity(
                                    Intent(activity, ParentsMainActivity::class.java)
                                        .putExtra(
                                            Constants.USER_TYPE,
                                            activity.intent.getStringExtra(Constants.USER_TYPE)
                                        )
                                )
                                activity.finish()
                            }

                        } catch (e: Exception) {
                        }
                    }
                }
            })
    }
}