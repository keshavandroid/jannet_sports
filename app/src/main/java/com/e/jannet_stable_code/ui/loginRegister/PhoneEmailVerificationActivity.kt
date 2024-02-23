package com.e.jannet_stable_code.ui.loginRegister

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.utils.Constants
import kotlinx.android.synthetic.main.activity_phone_email_varification.*
import kotlinx.android.synthetic.main.topbar_layout.*
import android.text.Editable

import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.CheckPhEmailOtpController
import com.e.jannet_stable_code.retrofit.controller.SendPhEmailOtpController
import com.e.jannet_stable_code.retrofit.response.SendOtpResponse
import com.e.jannet_stable_code.ui.loginRegister.addChildScreen.AddChildActivity
import com.e.jannet_stable_code.ui.parentsApp.ParentsMainActivity
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities

class PhoneEmailVerificationActivity : AppCompatActivity() {
    var emailOtp = ""
    var phoneOtp = ""

    var emailVarified = false
    var phoneVarified = false
    var email=""
    var id = ""
    var phone = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_email_varification)
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        setTopBar()
        setClicks()
        setOtpInputs()

        val storData = StoreUserData(this)
        val data = SharedPrefUserData(this).getSavedData()
        val participentEmail = data.email.toString()
        val participenPhone = data.contactno
        val participantId = data.id

        if (storData.getString(Constants.COACH_ID).trim().isEmpty()||storData.getString(Constants.COACH_ID).trim()==""||storData.getString(Constants.COACH_ID).trim()==""){

             email = data.email
            phone = data.contactno
             id = data.id
            Log.e(TAG, "onCreate: partcipent  login", )

        }else{

             email = storData.getString(Constants.COACH_Email)
             phone = storData.getString(Constants.COACH_PHONE)
             id = storData.getString(Constants.COACH_ID)

            Log.e(TAG, "onCreate: coach login", )

        }
        Log.e(TAG, "onCreate: id is ======$id", )
        SendPhEmailOtpController(
            context = this,
            email = "",
            phno = phone,
            userId = id,
            object : ControllerInterface {
                override fun onFail(error: String?) {

                }

                override fun <T> onSuccess(response: T, method: String) {
                    try {
                        val resp = response as SendOtpResponse

                        emailOtp = resp.getEmailOtp().toString()
                        phoneOtp = resp.getPhoneOtp().toString()


                        txtPhoneOtp.text = phoneOtp
                        txtEmailOtp.text = emailOtp
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        Log.d(TAG, "continueFlow: test>>"+SharedPrefUserData(this).getSavedData().usertype)


        tv_resend_Code_phone.setOnClickListener {

            SendPhEmailOtpController(
                context = this,
                email = "",
                phno = phone,
                userId = id,
                object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {
                        try {
                            val resp = response as SendOtpResponse

                            emailOtp = resp.getEmailOtp().toString()
                            phoneOtp = resp.getPhoneOtp().toString()


                            txtPhoneOtp.text = phoneOtp
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })


        }

        tv_resend_code_email.setOnClickListener {


            SendPhEmailOtpController(
                context = this,
                email = email,
                phno = "",
                userId = id,
                object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {
                        try {
                            val resp = response as SendOtpResponse

                            emailOtp = resp.getEmailOtp().toString()
                            phoneOtp = resp.getPhoneOtp().toString()


                            txtEmailOtp.text = emailOtp
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })



        }

    }



    private fun setOtpInputs() {
        singleInputStyle(null, etxtNo1, etxtNo2, 1)
        singleInputStyle(etxtNo1, etxtNo2, etxtNo3, 1)
        singleInputStyle(etxtNo2, etxtNo3, etxtNo4, 1)
        singleInputStyle(etxtNo3, etxtNo4, null, 1)

        singleInputStyle(null, etxtNo11, etxtNo12, 2)
        singleInputStyle(etxtNo11, etxtNo12, etxtNo13, 2)
        singleInputStyle(etxtNo12, etxtNo13, etxtNo14, 2)
        singleInputStyle(etxtNo13, etxtNo14, null, 2)
    }

    private fun singleInputStyle(
        editText0: EditText?,
        editText1: EditText,
        editText2: EditText?,
        int: Int
    ) {
        editText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                when {
                    editText2 == null && editText1.text.toString().length == 1 -> hideKeyboard()
                    editText1.text.toString().length == 1 -> editText2?.requestFocus()
                    else -> editText0?.requestFocus()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setClicks() {

        txtVerifyEmail.isEnabled=false
        txtVerifyEmail.isClickable=false
        txtVerifyEmail.setOnClickListener { txtVerifyPh.performClick() }
        txtVerifyPh.setOnClickListener {
            if (phoneVarified) {
                Utilities.showToast(this, "Phone number already verified.")
                continueFlow()
                return@setOnClickListener
            }

            val otpPhone =
                etxtNo1.text.toString() + etxtNo2.text.toString() + etxtNo3.text.toString() + etxtNo4.text.toString()
            when {
                otpPhone.length < 4 -> Utilities.showToast(
                    this,
                    "Please enter proper phone number otp to continue."
                )
                otpPhone != phoneOtp -> Utilities.showToast(this, "Incorrect Otp.")
                else -> {
                    phoneVarified=true
                    txtVerifyPh.text="Verified"
                    Utilities.showToast(this, "Phone number verified successfully.")
                    continueFlow()
                    txtVerifyPh.isEnabled=false
                    txtVerifyPh.isClickable = false
                    tv_resend_Code_phone.isEnabled = false
                    tv_resend_Code_phone.visibility = View.GONE
                    tv_resend_Code_phone.isClickable = false

                    txtVerifyEmail.isEnabled=true
                    txtVerifyEmail.isClickable=true
//                    val storData = StoreUserData(this)
//                    val email = storData.getString(Constants.COACH_Email)
//                    val phone = storData.getString(Constants.COACH_PHONE)
//                    val id = storData.getString(Constants.COACH_ID)

                    SendPhEmailOtpController(
                        context = this,
                        email = email,
                        phno = "",
                        userId = id,
                        object : ControllerInterface {
                            override fun onFail(error: String?) {

                            }

                            override fun <T> onSuccess(response: T, method: String) {
                                try {
                                    val resp = response as SendOtpResponse

                                    emailOtp = resp.getEmailOtp().toString()
                                    phoneOtp = resp.getPhoneOtp().toString()


                                    txtEmailOtp.text = emailOtp
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })


                }
            }
        }
        txtVerifyEmail.setOnClickListener {
            if (emailVarified) {
                Utilities.showToast(this, "Email already verified.")
                continueFlow()
                return@setOnClickListener
            }
            val otpEmail =
                etxtNo11.text.toString() + etxtNo12.text.toString() + etxtNo13.text.toString() + etxtNo14.text.toString()
            when {
                otpEmail.length < 4 -> Utilities.showToast(
                    this,
                    "Please enter proper email otp to continue."
                )
                otpEmail != emailOtp -> Utilities.showToast(this, "Incorrect Otp.")
                else -> {
                    emailVarified=true
                    txtVerifyEmail.text="Verified"
                    Utilities.showToast(this, "Email verified successfully.")
                    continueFlow()
                }
            }
        }
    }
    private val TAG = "PhoneEmailVerificationA"
    private fun continueFlow() {
        val usertype:String= SharedPrefUserData(this).getSavedData().usertype

        val stordata =  StoreUserData(this)
//        val id  = stordata.getString(Constants.COACH_ID)
        Log.d(TAG, "continueFlow: test>>"+usertype+">>"+phoneVarified+">>"+emailVarified+">>"+Constants.PARTICIPANT+">>"+Constants.COACH)

        if (phoneVarified && emailVarified){
            CheckPhEmailOtpController(context = this,emailOtp,phoneOtp,id.toString(),object :ControllerInterface{
                override fun onFail(error: String?) {

                }

                override fun <T> onSuccess(response: T, method: String) {
                    if (usertype=="parent") {
                        startActivity(
                            Intent(
                                this@PhoneEmailVerificationActivity,
                                AddChildActivity::class.java
                            ).putExtra(Constants.CHILD_FROM, Constants.EMAIL_VERIFICATION)
                        )
                    } else if (usertype=="adult") {
                        /*startActivity(
                            Intent(
                                this@PhoneEmailVerificationActivity,
                                SelectSportsActivity::class.java
                            ).putExtra(Constants.CHILD_FROM, Constants.EMAIL_VERIFICATION)
                        )*/

                        startActivity(
                            Intent(
                                this@PhoneEmailVerificationActivity,
                                ParentsMainActivity::class.java
                            ))


                    } else  {
                        startActivity(
                            Intent(
                                this@PhoneEmailVerificationActivity,
                                SelectSportsActivity::class.java
                            )
                        )
                    }

//                    startActivity(
//                            Intent(
//                                this@PhoneEmailVerificationActivity,
//                                SelectSportsActivity::class.java
//                            )
//                        )
                }
            })



        }
    }

    private fun setTopBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        imgLogo.visibility = View.GONE
        txtTitle.visibility = View.VISIBLE
        txtTitle.text = resources.getString(R.string.verification)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this@PhoneEmailVerificationActivity))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}