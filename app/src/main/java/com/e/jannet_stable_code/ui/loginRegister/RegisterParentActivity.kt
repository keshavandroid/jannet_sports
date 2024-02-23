package com.e.jannet_stable_code.ui.loginRegister

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.RegisterParentUserController
import com.e.jannet_stable_code.ui.loginRegister.loginScreen.LoginActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.SpinnerPickerDialog
import com.e.jannet_stable_code.utils.SpinnerPickerDialog.OnDialogListener
import com.e.jannet_stable_code.utils.Utilities
import kotlinx.android.synthetic.main.activity_parent_register.*
import kotlinx.android.synthetic.main.topbar_layout.*
import java.util.*


class RegisterParentActivity : AppCompatActivity() {
    var pickImage: PickImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_register)

      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setTopBar()

        clicks()

    }

    private fun clicks() {
        txtLogin.setOnClickListener {
            startActivity(
                Intent(this@RegisterParentActivity, LoginActivity::class.java).putExtra(
                    Constants.USER_TYPE, Constants.PARTICIPANT
                )
            )
            finish()
        }
        imgProfile.setOnClickListener {
            pickImage = PickImage(this@RegisterParentActivity)
        }

        imgMale.setOnClickListener { setGender(1) }
        txtMale.setOnClickListener { imgMale.performClick() }
        imgFemale.setOnClickListener { setGender(2) }
        txtFemale.setOnClickListener { imgFemale.performClick() }
        imgParent.setOnClickListener { setUserType(1) }
        txtParent.setOnClickListener { imgParent.performClick() }
        imgAdult.setOnClickListener { setUserType(2) }
        txtAdult.setOnClickListener { imgAdult.performClick() }
        txtBDate.setOnClickListener {
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
                    txtBDate.text="$year-"+(month+1)+"-$day"
                }

                override fun onCancel() {}
                override fun onDismiss() {}
            })
            spinnerPickerDialog.show(this.supportFragmentManager, "")

        }
        txtRegister.setOnClickListener {
            if (isDataValid()) {
                RegisterParentUserController(this, object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {
                        startActivity(
                            Intent(
                                this@RegisterParentActivity,
                                PhoneEmailVerificationActivity::class.java
                            ).putExtra(Constants.USER_TYPE, Constants.PARTICIPANT)
                        )
                    }
                }).callApi(registerObject!!)
            }
        }

    }

    private fun isDataValid(): Boolean {
        if (pickImage == null || pickImage!!.getImage() == null || pickImage!!.getImage()
                .equals("")
        ) {
            Utilities.showToast(this, "Please select image to continue.")
            return false
        } else if (etxtFName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter first name to continue.")
            return false
        } else if (etxtFName.text.toString().trim().length < 3) {
            Utilities.showToast(this, "Please enter longer first name to continue.")
            return false
        } else if (etxtLName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter last name to continue.")
            return false
        } else if (etxtLName.text.toString().trim().length < 3) {
            Utilities.showToast(this, "Please enter last name to continue.")
            return false
        } else if (etxtEmail.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail(etxtEmail.text.toString().trim())) {
            Utilities.showToast(this, "Please enter valid email to continue.")
            return false
        } else if (etxtPassword.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter password to continue.")
            return false
        } else if (etxtPassword.text.toString().trim().length < 6) {
            Utilities.showToast(this, "Please enter longer password to continue.")
            return false
        } else if (etxtPhNo.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter phone number to continue.")
            return false
        } else if (etxtPhNo.text.toString().trim().length < 6) {
            Utilities.showToast(this, "Please enter valid phone number to continue.")
            return false
        } else if (genderFlag == 0) {
            Utilities.showToast(this, "Please select gender to continue.")
            return false
        } else if(txtBDate.text.toString().trim()==""){
            Utilities.showToast(this, "Please enter birthdate to continue.")
            return false
        }/*else if (userTypeFlag == 0) {  // for usertype selection
            Utilities.showToast(this, "Please select user type to continue.")
            return false
        }*/else if (etxtMiddleName.text.toString().trim()==""){

            Utilities.showToast(this,"Please enter middle name to continue.")
        }

        registerObject = ParentRegisterObject()

        registerObject!!.email=etxtEmail.text.toString().trim()
        registerObject!!.password=etxtPassword.text.toString().trim()
        registerObject!!.firstname=etxtFName.text.toString().trim()
        registerObject!!.middlename=etxtMiddleName.text.toString().trim()
        registerObject!!.lastname=etxtLName.text.toString().trim()
        registerObject!!.contactNo=etxtPhNo.text.toString().trim()
        if(genderFlag==1) registerObject!!.gender="m"
        else if(genderFlag==2) registerObject!!.gender="f"
        registerObject!!.birthdate=txtBDate.text.toString().trim()

        registerObject!!.userType="parent"

        registerObject!!.image= pickImage!!.getImage()!!

        return true
    }

    var registerObject:ParentRegisterObject?=null
    var genderFlag = 0
    private fun setGender(i: Int) {
        genderFlag = i
        if (i == 1) {
            imgMale.setImageResource(R.mipmap.rad)
            imgFemale.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            imgMale.setImageResource(R.mipmap.rad1)
            imgFemale.setImageResource(R.mipmap.rad)
        }
    }

    var userTypeFlag = 0
    private fun setUserType(i: Int) {
        userTypeFlag = i
        if (i == 1) {
            imgParent.setImageResource(R.mipmap.rad)
            imgAdult.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            imgParent.setImageResource(R.mipmap.rad1)
            imgAdult.setImageResource(R.mipmap.rad)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(requestCode, resultCode, data, imgProfile)
    }

    private fun setTopBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        imgLogo.visibility = View.GONE
        txtTitle.visibility = View.VISIBLE
        txtTitle.textSize = 25F
        txtTitle.text = resources.getString(R.string.participant_register)
    }

    class ParentRegisterObject{
        var email=""
        var password=""
        var firstname=""
        var middlename=""
        var lastname=""
        var contactNo=""
        var gender=""
        var birthdate=""
        var userType=""
        var image=""
    }
}