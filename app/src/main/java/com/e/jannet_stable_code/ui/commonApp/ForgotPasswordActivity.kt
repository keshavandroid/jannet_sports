package com.e.jannet_stable_code.ui.commonApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityForgotPasswordBinding
import com.e.jannet_stable_code.databinding.ActivityStaticBinding
import com.e.jannet_stable_code.retrofit.controller.ForgotPasswordControllerr
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IForgotPasswordController
import com.e.jannet_stable_code.retrofit.forgotpassworddata
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.loginRegister.loginScreen.LoginActivity
import com.e.jannet_stable_code.viewinterface.iForgotPasswordView

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