package com.e.jannet_stable_code.ui.commonApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityChangePasswordBinding
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.ChangePwdController
import com.e.jannet_stable_code.utils.Utilities

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_change_password)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTopBar()


        binding.txtUpdatePassword.setOnClickListener {
            if (dataValid()) {
                ChangePwdController(this, object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {
                        finish()
                    }
                }).callApi(binding.etxtPwd.text.toString().trim(), binding.etxtConfirmPwd.text.toString().trim())

            }
        }

    }

    private fun dataValid(): Boolean {
        when {
            binding.etxtPwd.text.toString().trim() == "" -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Please enter current password to continue."
                )
                return false
            }
            binding.etxtPwd.text.toString().trim().length < 6 -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Current password is too short to continue."
                )
                return false
            }
            binding.etxtNewPwd.text.toString().trim() == "" -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Please enter new password to continue."
                )
                return false
            }
            binding.etxtNewPwd.text.toString().trim().length < 6 -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "New password is too short to continue."
                )
                return false
            }
            binding.etxtConfirmPwd.text.toString().trim() == "" -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Please enter confirm password to continue."
                )
                return false
            }
            binding.etxtConfirmPwd.text.toString().trim().length < 6 -> {
                Utilities.showToast(
                    this@ChangePasswordActivity,
                    "Confirm password is too short to continue."
                )
                return false
            }
            binding.etxtNewPwd.text.toString().trim() != binding.etxtConfirmPwd.text.toString().trim() -> {
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
        binding.includeTopbar.imgBack.setOnClickListener { finish() }
        binding.includeTopbar.txtTitle.text = getString(R.string.change_pwd)
    }
}