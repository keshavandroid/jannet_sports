package com.e.jannet_stable_code.ui.parentsApp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.AddChildListProfileAdapter
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.DeleteChildFromParentController
import com.e.jannet_stable_code.retrofit.controller.EditProfileParentController
import com.e.jannet_stable_code.retrofit.controller.GetProfileController
import com.e.jannet_stable_code.retrofit.response.ChildListObject
import com.e.jannet_stable_code.retrofit.response.GetProfileParentApiResponse
import com.e.jannet_stable_code.ui.loginRegister.addChildScreen.AddChildActivity
import com.e.jannet_stable_code.utils.*
import com.e.jannet_stable_code.utils.Constants.topChildData
import kotlinx.android.synthetic.main.activity_parent_update_profile.*
import kotlinx.android.synthetic.main.activity_parent_update_profile.imgFemale
import kotlinx.android.synthetic.main.activity_parent_update_profile.imgMale
import kotlinx.android.synthetic.main.activity_parent_update_profile.imgProfile
import kotlinx.android.synthetic.main.activity_parent_update_profile.txtBirthdate
import kotlinx.android.synthetic.main.activity_parent_update_profile.txtMale
import kotlinx.android.synthetic.main.item_child_list_profile.*
import kotlinx.android.synthetic.main.topbar_layout.*
import java.util.*

class ParentUpdateProfileActivity : AppCompatActivity() {
    var pickImage: PickImage? = null
    var addChildListAdapter: AddChildListProfileAdapter? = null
    var positionAdapter: Int = 0

    var tempUserData: ParentProfileObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_update_profile)
        tempUserData = ParentProfileObject()
        setTopBar()
        onClicks()
        setTab(1)


        val userType = SharedPrefUserData(this).getSavedData().usertype
        Log.d("USERTYPE0", "" + userType)

        if (userType.equals("adult")) {
            llSelectAccountType.visibility = View.GONE
        } else if (userType.equals("child")) {
            llSelectAccountType.visibility = View.GONE
        } else if (userType.equals("parent")) {

        }



        etxtName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.firstname = etxtName.text.toString().trim()
            }
        })
        etxtMiddleNameProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tempUserData!!.middlename = etxtMiddleNameProfile.text.toString().trim()

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        etxtLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.lastname = etxtLastName.text.toString().trim()
            }
        })
        etxtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.email = etxtEmail.text.toString().trim()
            }
        })
        etxtPhNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.phoneNumber = etxtPhNo.text.toString().trim()
            }
        })

        ivShowHide.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_UP -> etxtPassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    MotionEvent.ACTION_DOWN -> etxtPassword.setInputType(InputType.TYPE_CLASS_TEXT)
                }
                return true

            }

        })
    }

    override fun onResume() {
        super.onResume()

        GetProfileController(this, true, object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as GetProfileParentApiResponse
                    setUserData(data.getResult()!![0]!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun deleteChild(id: String) {
        DeleteChildFromParentController(this, object : ControllerInterface {
            override fun onFail(error: String?) {
                Utilities.showToast(this@ParentUpdateProfileActivity, error)
            }

            override fun <T> onSuccess(response: T, method: String) {
                Utilities.showToast(this@ParentUpdateProfileActivity, "Child deleted successfully.")
            }
        }).callApi(id)
    }

    private fun setUserData(result: GetProfileParentApiResponse.Result) {
        if (tempUserData!!.firstname == "")
            etxtName.setText(result.firstname)
        if (tempUserData!!.middlename == "")
            etxtMiddleNameProfile.setText(result.middleName)

        if (tempUserData!!.lastname == "")
            etxtLastName.setText(result.lastname)
        if (tempUserData!!.email == "")
            etxtEmail.setText(result.email)
        if (tempUserData!!.phoneNumber == "")
            etxtPhNo.setText(result.contactNo)
        if (tempUserData!!.gender == "") {
            if (result.gender == "m") setGender(1)
            else setGender(2)
        }
        if (result.userType == "adult") setUserType(2)
        else setUserType(1)

        if (tempUserData!!.birthdate == "")
            txtBirthdate.text = result.birthdate

        if (!isImagePickFlag)
            Glide.with(this)
                .load(result.image)
                .placeholder(R.mipmap.cam)
                .error(R.mipmap.cam)
                .into(imgProfile)

        setChildList(result.child!!)
    }

    private fun setChildList(child: List<GetProfileParentApiResponse.Child>) {
        val list = ArrayList<ChildListObject>()

        for (i in child.indices) {
            val childData = child[i]

            val singleItem = ChildListObject()
            singleItem.id = childData.id.toString()
            singleItem.firstName = childData.firstname.toString()
            singleItem.pwd = childData.password.toString()
            singleItem.middleName = childData.middleName.toString()
            singleItem.lastName = childData.lastname.toString()
            singleItem.jurseyName = childData.jurseyName.toString()
            singleItem.email = childData.email!!
            singleItem.pwd = ""
            singleItem.bdate = childData.birthdate!!

            var sportsStr = ""
            var sportsIDSStr = ""

            for (i in childData.sports!!.indices) {
                sportsStr = if (sportsStr == "") {
                    sportsIDSStr = childData.sports!![i].id.toString()
                    childData.sports!![i].sportsName!!
                } else {

                    sportsIDSStr = sportsIDSStr + ", " + childData.sports!![i].id.toString()
                    sportsStr + ", " + childData.sports!![i].sportsName
                }
            }

            singleItem.sports = sportsStr
            singleItem.sportsIDS = sportsIDSStr
            singleItem.imageUrl = childData.childImage!!
            if (childData.childGender != null)
                singleItem.gender = childData.childGender!!

            list.add(singleItem)
        }

        addChildListAdapter =
            AddChildListProfileAdapter(
                list,
                this,
                object : AddChildListProfileAdapter.AddChildlistInterface {
                    override fun onImagePickSelected(position: Int) {
                        positionAdapter = position
                        pickImage = PickImage(this@ParentUpdateProfileActivity)
                    }

                    override fun onEditChildSelected(position: Int, data: ChildListObject) {
                        topChildData = data

                        Log.d(TAG, "setUserData: ${topChildData!!.firstName}")
                        Log.d(TAG, "setUserData: ${topChildData!!.middleName}")
                        Log.d(TAG, "setUserData: ${topChildData!!.lastName}")
                        Log.d(TAG, "setUserData: ${topChildData!!.gradeId}")
                        Log.d(TAG, "setUserData: ${topChildData!!.email}")
                        Log.d(TAG, "setUserData: ${topChildData!!.pwd}")
                        Log.d(TAG, "setUserData: ${topChildData!!.bdate}")
                        Log.d(TAG, "setUserData: ${topChildData!!.sports}")
                        Log.d(TAG, "setUserData: ${topChildData!!.gender}")

                        //findViewById<TextView>(R.id.txtChildName).setText(firstName).toString().trim()
                        // txtChildJercyName_profile.setText(middleName)


                        startActivity(Intent(this@ParentUpdateProfileActivity, AddChildActivity::class.java)
                            .putExtra(Constants.CHILD_FROM, Constants.EDIT_CHILD)
                            .putExtra(Constants.CHILD_FIRSTNAME,topChildData!!.firstName)
                            .putExtra(Constants.CHILD_IMAGE,topChildData!!.childImage)
                        )
                    }

                    override fun onDeleteChildSelected(position: Int, id: String) {
                        deleteChild(id)
                        addChildListAdapter!!.deleteItem(position)
                    }

                })
        rcvChildListProfile.adapter = addChildListAdapter
    }

    private fun onClicks() {

        txtPersonal.setOnClickListener { setTab(1) }
        txtChild.setOnClickListener { setTab(2) }
        txtAddChild.setOnClickListener {
            startActivity(Intent(this@ParentUpdateProfileActivity, AddChildActivity::class.java)
                .putExtra(Constants.CHILD_FROM, Constants.ADD_CHILD)
            )
        }
        imgProfile.setOnClickListener {
            pickImage = PickImage(this@ParentUpdateProfileActivity)
        }
        txtEditPersonal.setOnClickListener {
            if (isUserDataValid()) {
                EditProfileParentController(this, object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {

                    }
                }).callApi(parentProfileObject!!)
            }
        }
        imgMale.setOnClickListener {
            setGender(1)
        }
        txtMale.setOnClickListener { imgMale.performClick() }
        imgFemale.setOnClickListener {
            setGender(2)
        }
        txtMale.setOnClickListener { imgFemale.performClick() }

        imgParent.setOnClickListener {
            //setUserType(1)
        }
        txtParent.setOnClickListener { imgParent.performClick() }
        imgAdult.setOnClickListener {
            //setUserType(2)
        }
        txtAdult.setOnClickListener { imgAdult.performClick() }
        txtBirthdate.setOnClickListener {
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
            spinnerPickerDialog.setOnDialogListener(object : SpinnerPickerDialog.OnDialogListener {
                @SuppressLint("SetTextI18n")
                override fun onSetDate(month: Int, day: Int, year: Int) {
                    // "  (Month selected is 0 indexed {0 == January})"
                    txtBirthdate.text = "$day-" + (month + 1) + "-$year"
                    tempUserData!!.birthdate = txtBirthDate.text.toString().trim()
                }

                override fun onCancel() {}
                override fun onDismiss() {}
            })
            spinnerPickerDialog.show(this.supportFragmentManager, "")
        }
    }

    var usertypeFlag = 0
    private fun setUserType(i: Int) {
        usertypeFlag = i
        if (i == 1) {
            imgParent.setImageResource(R.mipmap.rad)
            imgAdult.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            imgParent.setImageResource(R.mipmap.rad1)
            imgAdult.setImageResource(R.mipmap.rad)
        }
    }

    var genderFlag = 0
    private fun setGender(i: Int) {
        genderFlag = i
        if (i == 1) {
            imgMale.setImageResource(R.mipmap.rad)
            imgFemale.setImageResource(R.mipmap.rad1)
            tempUserData!!.gender = "m"
        } else if (i == 2) {
            imgMale.setImageResource(R.mipmap.rad1)
            imgFemale.setImageResource(R.mipmap.rad)
            tempUserData!!.gender = "f"
        }
    }

    var parentProfileObject: ParentProfileObject? = null
    private fun isUserDataValid(): Boolean {
        if (etxtName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter first name to continue.")
            return false
        } else if (etxtLastName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter last name to continue.")
            return false
        } else if (etxtEmail.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail(etxtEmail.text.toString().trim())) {
            Utilities.showToast(this, "Please enter a valid email to continue.")
            return false
        } else if (etxtPhNo.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter phone number to continue.")
            return false
        } else if (etxtPhNo.text.toString().trim().length < 6) {
            Utilities.showToast(this, "Please enter a valid phone number to continue.")
            return false
        } else if (genderFlag == 0) {
            Utilities.showToast(this, "Please select gender to continue.")
            return false
        } else if (txtBirthdate.text.toString().trim() == "") {
            Utilities.showToast(this, "Please select birthday to continue.")
            return false
        } else if (usertypeFlag == 0) {
            Utilities.showToast(this, "Please select user type to continue.")
            return false
        }

        parentProfileObject = ParentProfileObject()

        try {
            parentProfileObject!!.image = pickImage!!.getImage()!!
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        parentProfileObject!!.firstname = etxtName.text.toString().trim()
        parentProfileObject!!.password = etxtPassword.text.toString().trim()
        parentProfileObject!!.middlename = etxtMiddleNameProfile.text.toString().trim()
        parentProfileObject!!.lastname = etxtLastName.text.toString().trim()
        parentProfileObject!!.email = etxtEmail.text.toString().trim()
        parentProfileObject!!.phoneNumber = etxtPhNo.text.toString().trim()
        if (genderFlag == 1)
            parentProfileObject!!.gender = "m"
        if (genderFlag == 2)
            parentProfileObject!!.gender = "f"
        parentProfileObject!!.birthdate = txtBirthdate.text.toString().trim()
        parentProfileObject!!.type = "parent"
        return true
    }

    var isImagePickFlag = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) {
            pickImage!!.activityResult(requestCode, resultCode, data, imgProfile)

            if (pickImage!!.getImage() != null && pickImage!!.getImage() != "" && pickImage!!.getImage() != "null") {
                isImagePickFlag = true
            }
        }
    }

    private fun setTab(i: Int) {
        if (i == 1) {//personal
            txtPersonal.setBackgroundResource(R.mipmap.button_small)
            txtPersonal.setTextColor(resources.getColor(R.color.white))
            txtChild.setBackgroundResource(R.drawable.circle3)
            txtChild.setTextColor(resources.getColor(R.color.grey2))
            clPersonal.visibility = View.VISIBLE
            txtEditPersonal.visibility = View.VISIBLE
            llChild1.visibility = View.GONE
            llChild2.visibility = View.GONE
        } else if (i == 2) {//child
            txtPersonal.setBackgroundResource(R.drawable.circle3)
            txtPersonal.setTextColor(resources.getColor(R.color.grey2))
            txtChild.setBackgroundResource(R.mipmap.button_small)
            txtChild.setTextColor(resources.getColor(R.color.white))
            clPersonal.visibility = View.GONE
            txtEditPersonal.visibility = View.GONE
            llChild1.visibility = View.VISIBLE
            llChild2.visibility = View.VISIBLE
        }
    }

    private fun setTopBar() {
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.edit_profile_personal)
        txtTitle.textSize = 22F
    }
}

class ParentProfileObject {
    var image = ""
    var firstname = ""
    var password = ""
    var middlename = ""
    var lastname = ""
    var email = ""
    var phoneNumber = ""
    var gender = ""
    var birthdate = ""
    var type = ""
}