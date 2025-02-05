package com.xtrane.ui.loginRegister

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.xtrane.R
import com.xtrane.databinding.ActivityAdultRegisterBinding
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.RegisterParentUserController
import com.xtrane.ui.loginRegister.loginScreen.LoginActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.PickImage
import com.xtrane.utils.SpinnerPickerDialog
import com.xtrane.utils.SpinnerPickerDialog.OnDialogListener
import com.xtrane.utils.Utilities
import java.util.*


class RegisterAdultActivity : AppCompatActivity() {
    var pickImage: PickImage? = null
    private lateinit var binding: ActivityAdultRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /// setContentView(R.layout.activity_adult_register)
        binding = ActivityAdultRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setTopBar()

        clicks()

    }

    private fun clicks() {
        binding.txtLogin.setOnClickListener {
            startActivity(
                Intent(this@RegisterAdultActivity, LoginActivity::class.java).putExtra(
                    Constants.USER_TYPE, Constants.ADULT
                )
            )
            finish()
        }
        binding.imgProfile.setOnClickListener {
            pickImage = PickImage(this@RegisterAdultActivity)
        }

        binding.imgMale.setOnClickListener { setGender(1) }
        binding.txtMale.setOnClickListener { binding.imgMale.performClick() }
        binding.imgFemale.setOnClickListener { setGender(2) }
        binding.txtFemale.setOnClickListener { binding.imgFemale.performClick() }
        binding.imgParent.setOnClickListener { setUserType(1) }
        binding.txtParent.setOnClickListener { binding.imgParent.performClick() }
        binding.imgAdult.setOnClickListener { setUserType(2) }
        binding.txtAdult.setOnClickListener { binding.imgAdult.performClick() }
        binding.txtBDate.setOnClickListener {
            val maxCalendar = Calendar.getInstance()
            maxCalendar.add(Calendar.YEAR, -18)
            maxCalendar.add(Calendar.DAY_OF_MONTH, -1)

            val spinnerPickerDialog = SpinnerPickerDialog()
            spinnerPickerDialog.context = this
            spinnerPickerDialog.setMaxCalendar(maxCalendar)
            spinnerPickerDialog.setAllColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_green_dark
                )
            )
            spinnerPickerDialog.setmTextColor(Color.BLACK)
            spinnerPickerDialog.setArrowButton(true)
            spinnerPickerDialog.setOnDialogListener(object : OnDialogListener {
                @SuppressLint("SetTextI18n")
                override fun onSetDate(month: Int, day: Int, year: Int) {
                    // "  (Month selected is 0 indexed {0 == January})"
                    binding.txtBDate.text = "$year-" + (month + 1) + "-$day"
                }

                override fun onCancel() {}
                override fun onDismiss() {}
            })
            spinnerPickerDialog.show(this.supportFragmentManager, "")

        }
        binding.txtRegister.setOnClickListener {
            if (isDataValid()) {
                RegisterParentUserController(this, object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {
                        startActivity(
                            Intent(
                                this@RegisterAdultActivity,
                                PhoneEmailVerificationActivity::class.java
                            ).putExtra(Constants.USER_TYPE, Constants.ADULT)
                        )
                    }
                }).callApiAdult(registerObject!!)
            }
        }

    }

    private fun isDataValid(): Boolean {
        /*if (pickImage == null || pickImage!!.getImage() == null || pickImage!!.getImage()
                .equals("")
        ) {
            Utilities.showToast(this, "Please select image to continue.")
            return false
        } else */if ( binding.etxtFName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter first name to continue.")
            return false
        } else if ( binding.etxtFName.text.toString().trim().length < 3) {
            Utilities.showToast(this, "Please enter longer first name to continue.")
            return false
        }
//        else if (etxtLName.text.toString().trim() == "") {
//            Utilities.showToast(this, "Please enter last name to continue.")
//            return false
//        } else if (etxtLName.text.toString().trim().length < 3) {
//            Utilities.showToast(this, "Last name is too short")
//            return false
//        }
        else if ( binding.etxtEmail.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail( binding.etxtEmail.text.toString().trim())) {
            Utilities.showToast(this, "Please enter valid email to continue.")
            return false
        } else if ( binding.etxtPassword.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter password to continue.")
            return false
        } else if ( binding.etxtPassword.text.toString().trim().length < 6) {
            Utilities.showToast(this, "Please enter longer password to continue.")
            return false
        } else if ( binding.etxtPhNo.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter phone number to continue.")
            return false
        } else if ( binding.etxtPhNo.text.toString().trim().length < 6) {
            Utilities.showToast(this, "Please enter valid phone number to continue.")
            return false
        } else if (genderFlag == 0) {
            Utilities.showToast(this, "Please select gender to continue.")
            return false
        } else if ( binding.txtBDate.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter birthdate to continue.")
            return false
        }/*else if (userTypeFlag == 0) { // for userType selection
            Utilities.showToast(this, "Please select user type to continue.")
            return false
        }*/
        /*else if (etxtMiddleName.text.toString().trim()==""){

            Utilities.showToast(this,"Please enter middle name to continue.")
        }*/

        registerObject = ParentRegisterObject()

        registerObject!!.email =  binding.etxtEmail.text.toString().trim()
        registerObject!!.password =  binding.etxtPassword.text.toString().trim()
        registerObject!!.firstname =  binding.etxtFName.text.toString().trim()
        registerObject!!.middlename = "0"
        // registerObject!!.middlename=etxtMiddleName.text.toString().trim()
        //registerObject!!.lastname=etxtLName.text.toString().trim()
        registerObject!!.lastname = "0"
      //  registerObject!!.contactNo = etxtPhNo.text.toString().trim()
        registerObject!!.contactNo =  binding.etxtPhNo.text.toString().trim()+ binding.etxtPhNo1.text.toString().trim()+binding.etxtPhNo2.text.toString().trim()
        if (genderFlag == 1) registerObject!!.gender = "m"
        else if (genderFlag == 2) registerObject!!.gender = "f"
        registerObject!!.birthdate =  binding.txtBDate.text.toString().trim()

        registerObject!!.userType = "adult"

        if (pickImage != null && pickImage!!.getImage() != null) {
            registerObject!!.image = pickImage!!.getImage()!!
        }

        return true
    }

    var registerObject: ParentRegisterObject? = null
    var genderFlag = 0
    private fun setGender(i: Int) {
        genderFlag = i
        if (i == 1) {
            binding. imgMale.setImageResource(R.mipmap.rad)
            binding.imgFemale.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            binding.imgMale.setImageResource(R.mipmap.rad1)
            binding.imgFemale.setImageResource(R.mipmap.rad)
        }
    }

    var userTypeFlag = 0
    private fun setUserType(i: Int) {
        userTypeFlag = i
        if (i == 1) {
            binding.imgParent.setImageResource(R.mipmap.rad)
            binding.imgAdult.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            binding.imgParent.setImageResource(R.mipmap.rad1)
            binding.imgAdult.setImageResource(R.mipmap.rad)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(requestCode, resultCode, data, binding.imgProfile)
    }

    private fun setTopBar() {
        binding.topBar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.topBar.imgLogo.visibility = View.GONE
        binding.topBar.txtTitle.visibility = View.VISIBLE
        binding.topBar.txtTitle.textSize = 25F
        binding.topBar.txtTitle.text = resources.getString(R.string.adult_register)
    }

    class ParentRegisterObject {
        var email = ""
        var password = ""
        var firstname = ""
        var middlename = ""
        var lastname = ""
        var contactNo = ""
        var gender = ""
        var birthdate = ""
        var userType = ""
        var image = ""
    }
}