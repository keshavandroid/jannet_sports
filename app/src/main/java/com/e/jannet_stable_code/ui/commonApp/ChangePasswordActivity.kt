package com.e.jannet_stable_code.ui.commonApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.ChangePwdController
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.topbar_layout.*
import com.e.jannet_stable_code.utils.Utilities

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setTopBar()
        txtUpdatePassword.setOnClickListener {
            if (dataValid()) {
                ChangePwdController(this, object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {
                        finish()
                    }
                }).callApi(etxtPwd.text.toString().trim(), etxtConfirmPwd.text.toString().trim())

            }
        }

    }

    private fun dataValid(): Boolean {
        when {
            etxtPwd.text.toString().trim() == "" -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Please enter current password to continue."
                )
                return false
            }
            etxtPwd.text.toString().trim().length < 6 -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Current password is too short to continue."
                )
                return false
            }
            etxtNewPwd.text.toString().trim() == "" -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Please enter new password to continue."
                )
                return false
            }
            etxtNewPwd.text.toString().trim().length < 6 -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "New password is too short to continue."
                )
                return false
            }
            etxtConfirmPwd.text.toString().trim() == "" -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Please enter confirm password to continue."
                )
                return false
            }
            etxtConfirmPwd.text.toString().trim().length < 6 -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Confirm password is too short to continue."
                )
                return false
            }
            etxtNewPwd.text.toString().trim() != etxtConfirmPwd.text.toString().trim() -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "New passwords doesn't matched. Try again."
                )
                return false
            }
            else -> return true
        }
    }

    private fun setTopBar() {
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.change_pwd)
    }
}