package com.xtrane.ui.loginRegister.addChildScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xtrane.R
import com.xtrane.adapter.AddChildListAdapter
import com.xtrane.databinding.ActivityAddChildBinding
import com.xtrane.databinding.ActivityVenueBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.EditChildOfParentController
import com.xtrane.retrofit.controller.GetSportsListController
import com.xtrane.retrofit.response.ChildListObject
import com.xtrane.retrofit.response.SportsListResponse
import com.xtrane.ui.parentsApp.ParentsMainActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.Constants.topChildData
import com.xtrane.utils.PickImage
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.Utilities


class AddChildActivity : AppCompatActivity() {
    private var fromStr = ""
    var addChildListAdapter: AddChildListAdapter? = null
    var pickImage: PickImage? = null
    var positionAdapter: Int = 0
    private var TAG = "AddChildActivity"
    var firstName: String = ""
    var image: String = ""
    private lateinit var binding : ActivityAddChildBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_add_child)
        binding = ActivityAddChildBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        // firstName = intent.getStringExtra(Constants.CHILD_FIRSTNAME)!!
        //  image = intent.getStringExtra(Constants.CHILD_IMAGE)!!

        try {
            fromStr = intent.getStringExtra(Constants.CHILD_FROM)!!
            var llPwd = findViewById<LinearLayout>(R.id.llPwd)
            when (fromStr) {
                Constants.EDIT_CHILD -> {
                    binding.txtAdd.visibility = View.VISIBLE
                    binding.llAddFinish.visibility = View.GONE
                    binding.txtAdd.text = getString(R.string.save)
                    llPwd.visibility = View.VISIBLE
                    this.isEditFlag = true
                }
                Constants.ADD_CHILD -> {
                    binding.txtAdd.visibility = View.VISIBLE
                    llPwd.visibility = View.VISIBLE
                    binding.llAddFinish.visibility = View.GONE
                }
                Constants.EMAIL_VERIFICATION -> {
                    binding.txtAdd.visibility = View.GONE
                    binding.llAddFinish.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setTopBar()
        binding.txtFinish.setOnClickListener {
            hideKeyboard()
            if (isListDataValid()) {
                SaveChildsWithApi(addChildListAdapter!!.getListForApiCall(), this@AddChildActivity,
                    object : SaveChildsWithApi.OnSaveChildsWithApi {
                        override fun onSuccess() {
                            SharedPrefUserData(this@AddChildActivity).setRegisterStep("3")
                            startActivity(Intent(this@AddChildActivity,
                                ParentsMainActivity::class.java))
                            finish()
                        }

                        override fun onFail() {

                        }
                    })
            }

        }
        binding.txtAdd.setOnClickListener {
            hideKeyboard()

            if (isEditFlag) {
                if (isSingleDataValid(true)) {
                    EditChildOfParentController(this@AddChildActivity,
                        object : ControllerInterface {
                            override fun onFail(error: String?) {
                                Toast.makeText(this@AddChildActivity, error, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun <T> onSuccess(response: T, method: String) {
                                finish()
                            }
                        }).callApi(addChildListAdapter!!.getListForApiCall()[0])
                }
            } else {
                if (isSingleDataValid(false)) {
                    SaveChildsWithApi(addChildListAdapter!!.getListForApiCall(),
                        this@AddChildActivity,
                        object : SaveChildsWithApi.OnSaveChildsWithApi {
                            override fun onSuccess() {
                                Utilities.showToast(this@AddChildActivity,
                                    "Child added successfully")
                                finish()
                            }

                            override fun onFail() {

                            }
                        })
                }
            }
            //finish()
        }
        binding.txtAddAnotherChild.setOnClickListener {
            if (addChildListAdapter != null) {
                if (addChildListAdapter!!.itemCount <= 7)
                    addChildListAdapter!!.addNewItem()
                else Utilities.showToast(this, "Max 7 children can be added to a user.")
            }
        }


        //Adapter Set For Child

        val list = ArrayList<ChildListObject>()
        list.add(ChildListObject())
        addChildListAdapter =
            AddChildListAdapter(list, this, object : AddChildListAdapter.AddChildlistInterface {
                override fun onImagePickSelected(position: Int) {
                    positionAdapter = position
                    pickImage = PickImage(this@AddChildActivity)
                }

            })
        binding.rcvChildList.adapter = addChildListAdapter


        GetSportsListController(this, object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as SportsListResponse
                    addChildListAdapter!!.setSportsList(data.getResult()!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }).callApi()

//        if (isEditFlag)
//            binding.topBar.etxtChildName.setText(firstName)

//        setEditUserData()
    }

    private var isEditFlag = false

    private fun setEditUserData() {
        val data = topChildData
        Log.e("DATA", data.toString())
        addChildListAdapter!!.setEditChildUserData(data)
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this@AddChildActivity))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun isListDataValid(): Boolean {
        val list = addChildListAdapter!!.getListForApiCall()
        return AddChildScreenValidations(this@AddChildActivity).checkValidations(list, false,
            isEdit = false
        )
    }

    private fun isSingleDataValid(isEdit: Boolean): Boolean {
        val list = addChildListAdapter!!.getListForApiCall()
        return AddChildScreenValidations(this@AddChildActivity).checkValidations(list, true, isEdit)
    }


    private fun setTopBar() {
        binding.topBar.imgBack.setOnClickListener { onBackPressed() }
        if (isEditFlag)
            binding.topBar.txtTitle.text = getString(R.string.edit_child1)
        else
            binding.topBar.txtTitle.text = getString(R.string.add_child1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) {
            val imageStr = pickImage!!.activityResult(requestCode, resultCode, data, null)
            if (addChildListAdapter != null) {
                addChildListAdapter!!.setImageToPos(positionAdapter, imageStr)
            }
        } else {

        }
    }

    class ChildObject {
        var childId = ""
//        var childName = ""

        var firstName = ""
        var middleName = ""
        var lastName = ""
        var jurseyName = ""
        var gradeId = "1"
        var email = ""
        var password = ""
        var birthdate = ""
        var sportsIds = ""
        var childImage = ""
        var childGender = ""
        //password: String,

        fun setData(

            firstName: String,
            middleName: String,
            lastName: String,
            jurseyName: String,
            gradeId: String,
            email: String,
            birthdate: String,
            sportsIds: String,
            childImage: String?,
            childGender: String,
            password: String,
        )
        //this.password = password
        {
            this.firstName = firstName
            this.middleName = middleName
            this.lastName = lastName
            this.jurseyName = jurseyName
            this.gradeId = gradeId
            this.email = email
            this.birthdate = birthdate
            this.sportsIds = sportsIds
            this.childGender = childGender
            if (childImage != null) {
                this.childImage = childImage
            }
            this.password = password
            Log.e("FirstName", "setData: $firstName")
        }

        fun setChildId1(str: String) {
            this.childId = str
        }
    }
}