package com.xtrane.ui.parentsApp

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
import com.xtrane.R
import com.xtrane.adapter.AddChildListProfileAdapter
import com.xtrane.databinding.ActivityParentUpdateProfileBinding
import com.xtrane.databinding.ActivityRegisteredMatchBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.DeleteChildFromParentController
import com.xtrane.retrofit.controller.EditProfileParentController
import com.xtrane.retrofit.controller.GetProfileController
import com.xtrane.retrofit.response.ChildListObject
import com.xtrane.retrofit.response.GetProfileParentApiResponse
import com.xtrane.ui.loginRegister.addChildScreen.AddChildActivity
import com.xtrane.utils.*
import com.xtrane.utils.Constants.topChildData

import java.util.*

class ParentUpdateProfileActivity : AppCompatActivity() {
    var pickImage: PickImage? = null
    var addChildListAdapter: AddChildListProfileAdapter? = null
    var positionAdapter: Int = 0
    private lateinit var binding: ActivityParentUpdateProfileBinding

    var tempUserData: ParentProfileObject? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_parent_update_profile)
        binding = ActivityParentUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tempUserData = ParentProfileObject()
        setTopBar()
        onClicks()
        setTab(1)


        val userType = SharedPrefUserData(this).getSavedData().usertype
        Log.d("USERTYPE0", "" + userType)

        if (userType.equals("adult")) {
            binding.llSelectAccountType.visibility = View.GONE
        } else if (userType.equals("child")) {
            binding.llSelectAccountType.visibility = View.GONE
        } else if (userType.equals("parent")) {

        }



        binding.etxtName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.firstname = binding.etxtName.text.toString().trim()
            }
        })
        binding.etxtMiddleNameProfile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tempUserData!!.middlename = binding.etxtMiddleNameProfile.text.toString().trim()

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.etxtLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.lastname = binding.etxtLastName.text.toString().trim()
            }
        })
        binding.etxtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.email = binding.etxtEmail.text.toString().trim()
            }
        })
        binding.etxtPhNo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tempUserData!!.phoneNumber = binding.etxtPhNo.text.toString().trim()
            }
        })

        binding.ivShowHide.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_UP -> binding.etxtPassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    MotionEvent.ACTION_DOWN -> binding.etxtPassword.setInputType(InputType.TYPE_CLASS_TEXT)
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
            binding.etxtName.setText(result.firstname)
        if (tempUserData!!.middlename == "")
            binding.etxtMiddleNameProfile.setText(result.middleName)

        if (tempUserData!!.lastname == "")
            binding.etxtLastName.setText(result.lastname)
        if (tempUserData!!.email == "")
            binding.etxtEmail.setText(result.email)
        if (tempUserData!!.phoneNumber == "")
            binding.etxtPhNo.setText(result.contactNo)
        if (tempUserData!!.gender == "") {
            if (result.gender == "m") setGender(1)
            else setGender(2)
        }
        if (result.userType == "adult") setUserType(2)
        else setUserType(1)

        if (tempUserData!!.birthdate == "")
            binding.txtBirthdate.text = result.birthdate

        if (!isImagePickFlag)
            Glide.with(this)
                .load(result.image)
                .placeholder(R.mipmap.cam)
                .error(R.mipmap.cam)
                .into(binding.imgProfile)

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
        binding.rcvChildListProfile.adapter = addChildListAdapter
    }

    private fun onClicks() {

        binding.txtPersonal.setOnClickListener { setTab(1) }
        binding.txtChild.setOnClickListener { setTab(2) }
        binding.txtAddChild.setOnClickListener {
            startActivity(Intent(this@ParentUpdateProfileActivity, AddChildActivity::class.java)
                .putExtra(Constants.CHILD_FROM, Constants.ADD_CHILD)
            )
        }
        binding.imgProfile.setOnClickListener {
            pickImage = PickImage(this@ParentUpdateProfileActivity)
        }
        binding.txtEditPersonal.setOnClickListener {
            if (isUserDataValid()) {
                EditProfileParentController(this, object : ControllerInterface {
                    override fun onFail(error: String?) {

                    }

                    override fun <T> onSuccess(response: T, method: String) {

                    }
                }).callApi(parentProfileObject!!)
            }
        }
        binding.imgMale.setOnClickListener {
            setGender(1)
        }
        binding.txtMale.setOnClickListener { binding.imgMale.performClick() }
        binding.imgFemale.setOnClickListener {
            setGender(2)
        }
        binding.txtMale.setOnClickListener { binding.imgFemale.performClick() }

        binding.imgParent.setOnClickListener {
            //setUserType(1)
        }
        binding.txtParent.setOnClickListener { binding.imgParent.performClick() }
        binding.imgAdult.setOnClickListener {
            //setUserType(2)
        }
        binding.txtAdult.setOnClickListener { binding.imgAdult.performClick() }
        binding.txtBirthdate.setOnClickListener {
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
                    binding.txtBirthdate.text = "$day-" + (month + 1) + "-$year"
                    tempUserData!!.birthdate = binding.txtBirthDate.text.toString().trim()
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
            binding.imgParent.setImageResource(R.mipmap.rad)
            binding.imgAdult.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            binding.imgParent.setImageResource(R.mipmap.rad1)
            binding.imgAdult.setImageResource(R.mipmap.rad)
        }
    }

    var genderFlag = 0
    private fun setGender(i: Int) {
        genderFlag = i
        if (i == 1) {
            binding.imgMale.setImageResource(R.mipmap.rad)
            binding.imgFemale.setImageResource(R.mipmap.rad1)
            tempUserData!!.gender = "m"
        } else if (i == 2) {
            binding.imgMale.setImageResource(R.mipmap.rad1)
            binding.imgFemale.setImageResource(R.mipmap.rad)
            tempUserData!!.gender = "f"
        }
    }

    var parentProfileObject: ParentProfileObject? = null
    private fun isUserDataValid(): Boolean {
        if (binding.etxtName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter first name to continue.")
            return false
        } else if (binding.etxtLastName.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter last name to continue.")
            return false
        } else if (binding.etxtEmail.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail(binding.etxtEmail.text.toString().trim())) {
            Utilities.showToast(this, "Please enter a valid email to continue.")
            return false
        } else if (binding.etxtPhNo.text.toString().trim() == "") {
            Utilities.showToast(this, "Please enter phone number to continue.")
            return false
        } else if (binding.etxtPhNo.text.toString().trim().length < 6) {
            Utilities.showToast(this, "Please enter a valid phone number to continue.")
            return false
        } else if (genderFlag == 0) {
            Utilities.showToast(this, "Please select gender to continue.")
            return false
        } else if (binding.txtBirthdate.text.toString().trim() == "") {
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
        parentProfileObject!!.firstname = binding.etxtName.text.toString().trim()
        parentProfileObject!!.password = binding.etxtPassword.text.toString().trim()
        parentProfileObject!!.middlename = binding.etxtMiddleNameProfile.text.toString().trim()
        parentProfileObject!!.lastname = binding.etxtLastName.text.toString().trim()
        parentProfileObject!!.email = binding.etxtEmail.text.toString().trim()
        parentProfileObject!!.phoneNumber = binding.etxtPhNo.text.toString().trim()
        if (genderFlag == 1)
            parentProfileObject!!.gender = "m"
        if (genderFlag == 2)
            parentProfileObject!!.gender = "f"
        parentProfileObject!!.birthdate = binding.txtBirthdate.text.toString().trim()
        parentProfileObject!!.type = "parent"
        return true
    }

    var isImagePickFlag = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) {
            pickImage!!.activityResult(requestCode, resultCode, data, binding.imgProfile)

            if (pickImage!!.getImage() != null && pickImage!!.getImage() != "" && pickImage!!.getImage() != "null") {
                isImagePickFlag = true
            }
        }
    }

    private fun setTab(i: Int) {
        if (i == 1) {//personal
            binding.txtPersonal.setBackgroundResource(R.mipmap.button_small)
            binding.txtPersonal.setTextColor(resources.getColor(R.color.white))
            binding.txtChild.setBackgroundResource(R.drawable.circle3)
            binding. txtChild.setTextColor(resources.getColor(R.color.grey2))
            binding. clPersonal.visibility = View.VISIBLE
            binding.txtEditPersonal.visibility = View.VISIBLE
            binding.llChild1.visibility = View.GONE
            binding.llChild2.visibility = View.GONE
        } else if (i == 2) {//child
            binding.txtPersonal.setBackgroundResource(R.drawable.circle3)
            binding.txtPersonal.setTextColor(resources.getColor(R.color.grey2))
            binding.txtChild.setBackgroundResource(R.mipmap.button_small)
            binding.txtChild.setTextColor(resources.getColor(R.color.white))
            binding.clPersonal.visibility = View.GONE
            binding.txtEditPersonal.visibility = View.GONE
            binding. llChild1.visibility = View.VISIBLE
            binding.llChild2.visibility = View.VISIBLE
        }
    }

    private fun setTopBar() {
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text = getString(R.string.edit_profile_personal)
        binding.topbar.txtTitle.textSize = 22F
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