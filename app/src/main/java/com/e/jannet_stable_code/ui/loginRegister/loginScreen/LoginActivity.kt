package com.e.jannet_stable_code.ui.loginRegister.loginScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.coachlogindata.CoachLoginResult
import com.e.jannet_stable_code.retrofit.controller.CoachLoginController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.ICoachLoginController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.CoachMainActivity
import com.e.jannet_stable_code.ui.commonApp.ForgotPasswordActivity
import com.e.jannet_stable_code.ui.loginRegister.RegisterAdultActivity
import com.e.jannet_stable_code.ui.loginRegister.registerCoachScreen.RegisterCoachActivity
import com.e.jannet_stable_code.ui.loginRegister.RegisterParentActivity
import com.e.jannet_stable_code.ui.loginRegister.UserTypeSelectionActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.getUserType
import com.e.jannet_stable_code.viewinterface.ICoachLoginView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.topbar_layout.*

class LoginActivity : BaseActivity(),ICoachLoginView {
    var loginViewModel: LoginViewModel? = null
    lateinit var controller:ICoachLoginController

    lateinit var llRegisterBottom: LinearLayout

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViews()

        controller = CoachLoginController(this,this)
        loginViewModel = LoginViewModel(this)
        setTopBar()


        if (intent.getUserType() == Constants.COACH){
            txtLoginType.text = getString(R.string.coach_login)

            llRegisterBottom.visibility = View.VISIBLE

        }else if (intent.getUserType() == Constants.PARTICIPANT){
            txtLoginType.text = getString(R.string.participant_login)

            llRegisterBottom.visibility = View.VISIBLE
        }else if (intent.getUserType() == Constants.CHILD){
            txtLoginType.text = getString(R.string.child_login)

            llRegisterBottom.visibility = View.GONE
        }else if (intent.getUserType() == Constants.ADULT){
            txtLoginType.text = getString(R.string.adult_login)

            llRegisterBottom.visibility = View.VISIBLE
        }







        txtRegister.setOnClickListener {
            if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.COACH))
                startActivity(
                    Intent(this@LoginActivity, RegisterCoachActivity::class.java)
                )
            else if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.PARTICIPANT)) {
                startActivity(
                    Intent(this@LoginActivity, RegisterParentActivity::class.java)
                )
            }
            else if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.ADULT)) {
                startActivity(
                    Intent(this@LoginActivity, RegisterAdultActivity::class.java)
                )
            }
        }

        txtLogin.setOnClickListener {
            Log.e("TAG", "onLogin: " + intent.getStringExtra(Constants.USER_TYPE) )

            if (loginViewModel!!.dataValid(
                    email = etxtEmail.text.toString().trim(),
                    pwd = etxtPassword.text.toString().trim()
                )
            ) {
                if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.COACH)) {
                    Log.e("TAG", "onCreate: upside new coach api ", )
                    controller.callCoachLoginApi(etxtEmail.text.toString().trim(),etxtPassword.text.toString().trim())
                    showLoader()
                }
                else if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.CHILD)){

                    //child login
                    loginViewModel!!.proceedLogin("child")


                }else if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.PARTICIPANT)){
                    Log.e("TAG", "onCreate: not call new coach api ", )

                    //parent login
                    loginViewModel!!.proceedLogin("parent")
                }else if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.ADULT)){
                    Log.e("TAG", "onCreate: Call Adult api " )

                    //parent login
                    loginViewModel!!.proceedLogin("adult")
                }
            }
        }

        tv_forgotPassword.setOnClickListener {


            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun findViews() {
        llRegisterBottom = findViewById(R.id.llRegisterBottom);
    }

    override fun onBackPressed() {
        startActivity(Intent(this@LoginActivity, UserTypeSelectionActivity::class.java))
        finish()
    }

    private fun setTopBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        imgLogo.visibility = View.VISIBLE
        txtTitle.visibility = View.GONE
    }

    override fun onCoachLoginSuccess(response: CoachLoginResult) {
        Log.e("TAG", "onCoachLoginSuccess: success$response", )
        val storeData = StoreUserData(this)
        val  id = response.getId().toString()
        val token = response.getUserToken().toString()
        val userType = response.getUserType()
        val email = response.getEmail().toString()
        val phone = response.getContactNo().toString()

        storeData.setString(Constants.COACH_ID,id)
        storeData.setString(Constants.COACH_TOKEN,token)
        storeData.setString(Constants.COACH_TYPE,userType)
        storeData.setString(Constants.COACH_Email,email.toString())
        storeData.setString(Constants.COACH_PHONE,phone.toString())

        if (userType != null) {
            SharedPrefUserData(this).saveCoachLoginResp(userType)
        }


        hideLoader()
        startActivity(
            Intent(this, CoachMainActivity::class.java)
                .putExtra(Constants.USER_TYPE, intent.getStringExtra(Constants.USER_TYPE)))
        finish()
    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }
}