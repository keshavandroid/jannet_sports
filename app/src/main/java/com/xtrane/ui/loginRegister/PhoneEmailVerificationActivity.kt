package com.xtrane.ui.loginRegister

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xtrane.R
import com.xtrane.utils.Constants
import android.text.Editable

import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.databinding.ActivityPhoneEmailVarificationBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.CheckPhEmailOtpController
import com.xtrane.retrofit.controller.SendPhEmailOtpController
import com.xtrane.retrofit.response.SendOtpResponse
import com.xtrane.ui.loginRegister.addChildScreen.AddChildActivity
import com.xtrane.ui.parentsApp.ParentsMainActivity
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities

class PhoneEmailVerificationActivity : AppCompatActivity() {
    var emailOtp = ""
    var phoneOtp = ""

    var emailVarified = false
    var phoneVarified = false
    var email=""
    var id = ""
    var phone = ""
    private lateinit var binding: ActivityPhoneEmailVarificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_email_varification)
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = ActivityPhoneEmailVarificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


                        binding.txtPhoneOtp.text = phoneOtp
                        binding.txtEmailOtp.text = emailOtp
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        Log.d(TAG, "continueFlow: test>>"+SharedPrefUserData(this).getSavedData().usertype)


        binding.tvResendCodePhone.setOnClickListener {

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


                            binding.txtPhoneOtp.text = phoneOtp
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })


        }

        binding.tvResendCodeEmail.setOnClickListener {


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


                            binding.txtEmailOtp.text = emailOtp
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })



        }

    }



    private fun setOtpInputs() {
        singleInputStyle(null, binding.etxtNo1, binding.etxtNo2, 1)
        singleInputStyle(binding.etxtNo1, binding.etxtNo2, binding.etxtNo3, 1)
        singleInputStyle(binding.etxtNo2, binding.etxtNo3, binding.etxtNo4, 1)
        singleInputStyle(binding.etxtNo3, binding.etxtNo4, null, 1)

        singleInputStyle(null, binding.etxtNo11, binding.etxtNo12, 2)
        singleInputStyle(binding.etxtNo11, binding.etxtNo12, binding.etxtNo13, 2)
        singleInputStyle(binding.etxtNo12, binding.etxtNo13, binding.etxtNo14, 2)
        singleInputStyle(binding.etxtNo13, binding.etxtNo14, null, 2)
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

        binding.txtVerifyEmail.isEnabled=false
        binding.txtVerifyEmail.isClickable=false
        binding.txtVerifyEmail.setOnClickListener { binding.txtVerifyPh.performClick() }
        binding.txtVerifyPh.setOnClickListener {
            if (phoneVarified) {
                Utilities.showToast(this, "Phone number already verified.")
                continueFlow()
                return@setOnClickListener
            }

            val otpPhone =
                binding.etxtNo1.text.toString() + binding.etxtNo2.text.toString() + binding.etxtNo3.text.toString() + binding.etxtNo4.text.toString()
            when {
                otpPhone.length < 4 -> Utilities.showToast(
                    this,
                    "Please enter proper phone number otp to continue."
                )
                otpPhone != phoneOtp -> Utilities.showToast(this, "Incorrect Otp.")
                else -> {
                    phoneVarified=true
                    binding.txtVerifyPh.text="Verified"
                    Utilities.showToast(this, "Phone number verified successfully.")
                    continueFlow()
                    binding.txtVerifyPh.isEnabled=false
                    binding.txtVerifyPh.isClickable = false
                    binding.tvResendCodePhone.isEnabled = false
                    binding.tvResendCodePhone.visibility = View.GONE
                    binding.tvResendCodePhone.isClickable = false

                    binding.txtVerifyEmail.isEnabled=true
                    binding.txtVerifyEmail.isClickable=true
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


                                    binding.txtEmailOtp.text = emailOtp
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })


                }
            }
        }
        binding.txtVerifyEmail.setOnClickListener {
            if (emailVarified) {
                Utilities.showToast(this, "Email already verified.")
                continueFlow()
                return@setOnClickListener
            }
            val otpEmail =
                binding.etxtNo11.text.toString() + binding.etxtNo12.text.toString() + binding.etxtNo13.text.toString() + binding.etxtNo14.text.toString()
            when {
                otpEmail.length < 4 -> Utilities.showToast(
                    this,
                    "Please enter proper email otp to continue."
                )
                otpEmail != emailOtp -> Utilities.showToast(this, "Incorrect Otp.")
                else -> {
                    emailVarified=true
                    binding.txtVerifyEmail.text="Verified"
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
        binding.topBar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.topBar.imgLogo.visibility = View.GONE
        binding.topBar.txtTitle.visibility = View.VISIBLE
        binding.topBar.txtTitle.text = resources.getString(R.string.verification)
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