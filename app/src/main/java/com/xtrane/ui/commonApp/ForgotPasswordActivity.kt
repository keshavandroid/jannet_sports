package com.xtrane.ui.commonApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xtrane.R
import com.xtrane.databinding.ActivityForgotPasswordBinding
import com.xtrane.databinding.ActivityStaticBinding
import com.xtrane.retrofit.controller.ForgotPasswordControllerr
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IForgotPasswordController
import com.xtrane.retrofit.forgotpassworddata
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.loginRegister.loginScreen.LoginActivity
import com.xtrane.viewinterface.iForgotPasswordView

class ForgotPasswordActivity : BaseActivity(), iForgotPasswordView {

    lateinit var controller: IForgotPasswordController
    override fun getController(): IBaseController? {
        return null
    }
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_forgot_password)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = ForgotPasswordControllerr(this, this)
        setTopBar()

        binding. btnSend.setOnClickListener {

            if (binding.etxtEmailForgot.text.toString().trim().isEmpty() && binding.etxtEmailForgot.text.toString().trim() == null
            )
            {
                showToast("Please Enter Email Address")
            }
            else {
                controller.callForgotPasswordApi(binding.etxtEmailForgot.text.toString().trim())
                showLoader()
            }

        }

    }

    fun setTopBar() {


        binding.include.txtTitle.text = "FORGOT PASSWORD"
        binding.include.imgBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onForgotPasswordSuccess(response: forgotpassworddata) {
        hideLoader()
        showToast(response.getMessage())
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
    }
}