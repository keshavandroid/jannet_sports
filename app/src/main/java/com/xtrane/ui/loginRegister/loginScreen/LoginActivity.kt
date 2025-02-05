package com.xtrane.ui.loginRegister.loginScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.xtrane.R
import com.xtrane.databinding.ActivityEditMainTeamBinding
import com.xtrane.databinding.ActivityLoginBinding
import com.xtrane.retrofit.coachlogindata.CoachLoginResult
import com.xtrane.retrofit.controller.CoachLoginController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachLoginController
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.coachApp.CoachMainActivity
import com.xtrane.ui.commonApp.ForgotPasswordActivity
import com.xtrane.ui.loginRegister.RegisterAdultActivity
import com.xtrane.ui.loginRegister.registerCoachScreen.RegisterCoachActivity
import com.xtrane.ui.loginRegister.RegisterParentActivity
import com.xtrane.ui.loginRegister.UserTypeSelectionActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.getUserType
import com.xtrane.viewinterface.ICoachLoginView

class LoginActivity : BaseActivity(),ICoachLoginView {
    var loginViewModel: LoginViewModel? = null
    lateinit var controller:ICoachLoginController

    lateinit var llRegisterBottom: LinearLayout
    private lateinit var binding: ActivityLoginBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViews()

        controller = CoachLoginController(this,this)
        loginViewModel = LoginViewModel(this)
        setTopBar()


        if (intent.getUserType() == Constants.COACH){
            binding.txtLoginType.text = getString(R.string.coach_login)

            llRegisterBottom.visibility = View.VISIBLE

        }else if (intent.getUserType() == Constants.PARTICIPANT){
            binding.txtLoginType.text = getString(R.string.participant_login)

            llRegisterBottom.visibility = View.VISIBLE
        }else if (intent.getUserType() == Constants.CHILD){
            binding.txtLoginType.text = getString(R.string.child_login)

            llRegisterBottom.visibility = View.GONE
        }else if (intent.getUserType() == Constants.ADULT){
            binding.txtLoginType.text = getString(R.string.adult_login)

            llRegisterBottom.visibility = View.VISIBLE
        }

        binding.txtRegister.setOnClickListener {
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

        binding.txtLogin.setOnClickListener {
            Log.e("TAG", "onLogin: " + intent.getStringExtra(Constants.USER_TYPE) )

            if (loginViewModel!!.dataValid(
                    email =   binding.etxtEmail.text.toString().trim(),
                    pwd =   binding.etxtPassword.text.toString().trim()
                )
            ) {
                if (intent.getStringExtra(Constants.USER_TYPE).equals(Constants.COACH)) {
                    Log.e("TAG", "onCreate: upside new coach api ", )
                    controller.callCoachLoginApi(  binding.etxtEmail.text.toString().trim(),  binding.etxtPassword.text.toString().trim())
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

        binding.tvForgotPassword.setOnClickListener {


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
        binding.topbar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.topbar.imgLogo.visibility = View.VISIBLE
        binding.topbar.txtTitle.visibility = View.GONE
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