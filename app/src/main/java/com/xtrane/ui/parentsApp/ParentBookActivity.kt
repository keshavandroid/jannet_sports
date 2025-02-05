package com.xtrane.ui.parentsApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.xtrane.R
import com.xtrane.adapter.ChildInfoAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityParentBookBinding
import com.xtrane.multispinner.KeyPairBoolData
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.childinfodata.ChildInfoResult
import com.xtrane.retrofit.controller.ChildInfoController
import com.xtrane.retrofit.controller.GetProfileController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IChildInfoController
import com.xtrane.retrofit.response.GetProfileParentApiResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.IChildInfoView
class ParentBookActivity : BaseActivity(), IChildInfoView {
    val arrayList = ArrayList<String>()
    val childList = ArrayList<GetProfileParentApiResponse.Child>()
    val TAG = "ParentBookActivity"
    lateinit var controller: IChildInfoController
    var selectChildId: String = "0"
    private lateinit var binding: ActivityParentBookBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_parent_book)
        binding = ActivityParentBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        controller = ChildInfoController(this, this)

        binding.txtSelectChild.setOnClickListener { binding.spinnerChildList.performClick() }

        binding.txtBook.setOnClickListener {
//            if (isDataValid()) {
            if (selectChildId == "0" && selectChildId == null) {
                showToast("Please Select Child..")
            } else {

                val eventid = intent.getStringExtra("eventId")


//                startActivity(
//                    Intent(
//                        this,
//                        BookSignatureActivity::class.java
//                    )
//                )

                val fees = binding.txtFess.text.toString()
                val intent = Intent(this, BookSignatureActivity::class.java)
                intent.putExtra("eventId", eventid.toString())
                intent.putExtra("ChildId", selectChildId.toString())
                intent.putExtra("Fees", fees.toString())

                Log.e("DATA", eventid.toString() + "" + selectChildId + "" + fees + "")

                startActivity(intent)
                finish()
            }
        }
//        }
        /*imgMale.setOnClickListener { setGender(1) }
        txtMale.setOnClickListener { imgMale.performClick() }
        imgFemale.setOnClickListener { setGender(2) }
        txtFemale.setOnClickListener { imgFemale.performClick() }*/


        GetProfileController(this, true, object : ControllerInterface {
            override fun onFail(error: String?) {

                hideLoader()
                showToast(error)
            }

            override fun <T> onSuccess(response: T, method: String) {

                hideLoader()
                try {

                    val data = response as GetProfileParentApiResponse
                    val res = data.getResult()

//
                    if (res!![0]!!.child!!.isNotEmpty()) {

                        setChildList(data.getResult())

                    } else {


                        showToast("Please Add Child First...")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        Constants.eventDetailTop
    }

    private fun setChildList(result: List<GetProfileParentApiResponse.Result?>?) {
//        arrayList.clear()
//        childList.clear()
//
//
//
//
//
//        for (i in result!![0]!!.child!!.indices) {
//            arrayList.add(result[0]!!.child!![i].name!!)
//            childList.add(result[0]!!.child!![i])
//        }
//
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
//        spinnerChildList.adapter = adapter
//
//        spinnerChildList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long,
//            ) {
//                txtSelectChild.text = arrayList[position]
//                etxtName.setText(childList[position].name)
//
//                Log.e(TAG, " child id======${childList[position].id}")
//
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }
//        Log.e(TAG,"1====${result!![0]!!.child!!.size}")
//        if (result!![0]!!.child!!.isNotEmpty()) {
//
//            Log.e(TAG,"2====${result!![0]!!.child!!.size}")

        val listArray1: MutableList<KeyPairBoolData> = java.util.ArrayList()

        try {

        } catch (e: Exception) {

            showToast(e.toString())
        }
        for (i in result!![0]!!.child!!.indices) {
            val h = KeyPairBoolData()
            h.id = result[0]!!.child!![i].id!!.toLong()
            h.name = result[0]!!.child!![i].firstname.toString()
            listArray1.add(h)
        }


//                    Log.e(TAG,"==========name is =====${data.getResult()!![1]?.name}")
        binding.multipleItemSelectionSpinner.isSearchEnabled = false;

        // A text that will display in search hint.
        binding.multipleItemSelectionSpinner.setSearchHint("Select");
        // Set text that will display when search result not found...
        binding.multipleItemSelectionSpinner.setEmptyTitle("Not Data Found!");

        // If you will set the limit, this button will not display automatically.
        binding.multipleItemSelectionSpinner.setShowSelectAllButton(true);

        //A text that will display in clear text button
        binding.multipleItemSelectionSpinner.setClearText("Close & Clear");
        binding.multipleItemSelectionSpinner.isSelected = false


//            if (multipleItemSelectionSpinner.count == 0) {
//
//
//            } else {

        binding.multipleItemSelectionSpinner.setItems(listArray1) { childrenResult ->

            try {
                for (i in childrenResult.indices) {
                    if (childrenResult[i].isSelected) {

                        Log.i(TAG,
                            i.toString() + " : " + childrenResult[i].name + " : " + childrenResult[i].isSelected)


//                     selectedLocationType = multipleItemSelectionSpinner.selectedItem
                        selectChildId =
                            binding.multipleItemSelectionSpinner.selectedIds.joinToString(",")

//                    val str: String = java.lang.String.join(",", selectChildId as String)

//                    Log.e(TAG,"selected item, list ===========$selectedLocationType")
                        Log.e(TAG, "selected item, list ===========$selectChildId")
                        Log.e(TAG, "selected item, list string ===========$selectChildId")

                        Log.e(TAG, " child id======${childrenResult[i].id}")

                        val id = SharedPrefUserData(this@ParentBookActivity).getSavedData().id
                        val token = SharedPrefUserData(this@ParentBookActivity).getSavedData().token

                        showLoader()
                        controller.callChildInfoAPI(id, token, selectChildId)
                        binding. llTotalPrice.isVisible = true
                        binding.txtBook.isVisible = true

                    }

                }

            } catch (e: Exception) {

                showToast(e.toString())
            }

        }

//            }


    }

    var genderFlag = 0
    private fun setGender(i: Int) {
        genderFlag = i
        if (i == 1) {
            binding.imgMale.setImageResource(R.mipmap.rad)
            binding.imgFemale.setImageResource(R.mipmap.rad1)
        } else if (i == 2) {
            binding.imgMale.setImageResource(R.mipmap.rad1)
            binding.imgFemale.setImageResource(R.mipmap.rad)
        }
    }

    private fun isDataValid(): Boolean {
        if (binding.spinnerChildList.selectedItemPosition == 0) {
            Utilities.showToast(this@ParentBookActivity, "Please select child to continue.")
            return false
        } else if (binding.etxtName.text.toString().trim() == "") {
            Utilities.showToast(this@ParentBookActivity, "Please enter name to continue.")
            return false
        } else if (genderFlag == 0) {
            Utilities.showToast(this@ParentBookActivity, "Please select gender to continue.")
            return false
        } else if (binding.etxtEmail.text.toString().trim() == "") {
            Utilities.showToast(this@ParentBookActivity, "Please enter email to continue.")
            return false
        } else if (!Utilities.isValidEmail(binding.etxtEmail.text.toString().trim())) {
            Utilities.showToast(this@ParentBookActivity, "Please enter a valid email to continue.")
            return false
        } else if (binding.etxtPhNo.text.toString().trim() == "") {
            Utilities.showToast(this@ParentBookActivity, "Please enter phone number to continue.")
            return false
        } else if (binding.etxtPhNo.text.toString().trim().length < 6) {
            Utilities.showToast(
                this@ParentBookActivity,
                "Please enter a valid phone number to continue."
            )
            return false
        }


        return true
    }

    private fun setTopBar() {
        binding.includeTopbar.imgBack.visibility = View.VISIBLE
        binding.includeTopbar.imgBack.setOnClickListener { finish() }
        binding.includeTopbar.txtTitle.text = getString(R.string.parent_book)
    }

    override fun onChildInfoSuccess(response: List<ChildInfoResult?>?) {


        hideLoader()
        var childAdapter = ChildInfoAdapter(this, response)
        binding.rvChildList.adapter = childAdapter
        childAdapter.notifyDataSetChanged()


    }

    override fun onFail(message: String?, e: Exception?) {

//        showToast(message)
        hideLoader()

    }
}