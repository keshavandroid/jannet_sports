package com.e.jannet_stable_code.ui.loginRegister.registerCoachScreen

import android.content.Intent
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.RegisterCoachUserController
import com.e.jannet_stable_code.retrofit.response.CoachRegisterResponse
import com.e.jannet_stable_code.ui.loginRegister.PhoneEmailVerificationActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities

class RegisterCoachModel(val activity: RegisterCoachActivity) {
    var data: RegisterCoachActivity.CoachRegisterObject? = null
    fun registerValidation(
        data: RegisterCoachActivity.CoachRegisterObject
    ): Boolean {
        if (data.image == null || data.image == "") {
            Utilities.showToast(activity, "Please select image to continue.")
            return false
        } else if (data.name == "") {
            Utilities.showToast(activity, "Please enter name to continue.")
            return false
        } else if (data.email == "") {
            Utilities.showToast(activity, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail(data.email)) {
            Utilities.showToast(activity, "Please enter valid email to continue.")
            return false
        } else if (data.password == "") {
            Utilities.showToast(activity, "Please enter password to continue.")
            return false
        } else if (data.password.length < 6) {
            Utilities.showToast(activity, "Please enter valid password to continue.")
            return false
        } else if (data.contactNo == "") {
            Utilities.showToast(activity, "Please enter phone number to continue.")
            return false
        } else if (data.contactNo.length < 6) {
            Utilities.showToast(activity, "Please enter valid phone number to continue.")
            return false
        } else if (data.birthdate == "") {
            Utilities.showToast(activity, "Please select birth date to continue.")
            return false
        } else if (data.location == "") {
            Utilities.showToast(activity, "Please enter location to continue.")
            return false
        } else if (data.gender == 0) {
            Utilities.showToast(activity, "Please select gender to continue.")
            return false
        }

        this.data = data
        return true
    }

    fun registerCoach() {
        if (data == null) return

        RegisterCoachUserController(activity, object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {


                val data = response as CoachRegisterResponse

                val stordata = StoreUserData(activity)
                stordata.setString(Constants.COACH_ID, data.getResult()?.id.toString())
                stordata.setString(Constants.COACH_TOKEN, data.getResult()?.userToken.toString())
                stordata.setString(Constants.COACH_Email, data.getResult()?.email.toString())
                stordata.setString(Constants.COACH_PHONE, data.getResult()?.contactNo.toString())
                activity.startActivity(
                    Intent(
                        activity,
                        PhoneEmailVerificationActivity::class.java
                    ).putExtra(Constants.USER_TYPE, Constants.COACH)
                )
            }
        }).callApi(data!!)
    }
}