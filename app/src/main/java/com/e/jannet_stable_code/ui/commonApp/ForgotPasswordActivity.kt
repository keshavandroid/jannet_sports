package com.e.jannet_stable_code.ui.commonApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.ForgotPasswordControllerr
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IForgotPasswordController
import com.e.jannet_stable_code.retrofit.forgotpassworddata
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.loginRegister.loginScreen.LoginActivity
import com.e.jannet_stable_code.viewinterface.iForgotPasswordView
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.topbar_layout.*

class ForgotPasswordActivity : BaseActivity(), iForgotPasswordView {

    lateinit var controller: IForgotPasswordController
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        controller = ForgotPasswordControllerr(this, this)
        setTopBar()

        btnSend.setOnClickListener {

            if (etxtEmailForgot.text.toString().trim().isEmpty() && etxtEmailForgot.text.toString().trim() == null
            ) {

                showToast("Please Enter Email Address")
            } else {


                controller.callForgotPasswordApi(etxtEmailForgot.text.toString().trim())
                showLoader()
            }

        }

    }

    fun setTopBar() {


        txtTitle.text = "FORGOT PASSWORD"
        imgBack.setOnClickListener {
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