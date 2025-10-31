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
import com.xtrane.retrofit.response.EventDetailResponse
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
    var eventData: EventDetailResponse.Result? = null
    var counter = 0
    var parentID = ""
    var fees = ""
    var finalfees = 0

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_parent_book)
        binding = ActivityParentBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        eventData = Constants.eventDetailTop

        if (intent.hasExtra("Fees")) {
            fees = intent.extras!!.getString("Fees").toString()

        } else {
            if (eventData != null && eventData!!.getFees()!!.length > 0)
                fees = eventData!!.getFees().toString()
        }

        controller = ChildInfoController(this, this)

        binding.txtSelectChild.setOnClickListener { binding.spinnerChildList.performClick() }

        binding.checkboxSelectAll.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                parentID = SharedPrefUserData(this@ParentBookActivity).getSavedData().id.toString()
                // Get the total number of children from the spinner
                val totalChildren = binding.multipleItemSelectionSpinner.selectedIds.size
                if (finalfees>0) {
                    finalfees = fees.toInt() + finalfees

                } else {
                    finalfees = fees.toInt()

                }
                binding.txtFess.text = finalfees.toString()
                binding.txtBook.visibility=View.VISIBLE
            } else {
                parentID = ""
                finalfees = finalfees-fees.toInt()
                binding.txtFess.text = finalfees.toString()
                binding.txtBook.visibility=View.GONE

            }
        }



        binding.txtBook.setOnClickListener {
//            if (isDataValid()) {
            if (selectChildId == "0" && selectChildId == null) {
                showToast("Please Select Child..")
            } else {
                val eventid = intent.getStringExtra("eventId")
                val fees = binding.txtFess.text.toString()
                //  val fees = eventData!!.getFees()!!.toInt()*counter

                val intent = Intent(this, BookSignatureActivity::class.java)
                intent.putExtra("eventId", eventid.toString())
                intent.putExtra("ChildId", selectChildId.toString())
                intent.putExtra("Fees", fees.toString())
                intent.putExtra("parentID", parentID)

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
                counter = 0 // Reset counter each time the dialog is opened and items are selected

                // Reset counter each time the dialog is opened and items are selected
                for (i in childrenResult.indices) {

                    if (childrenResult[i].isSelected) {
                        Log.e("", "child is selected");

                        Log.i(
                            TAG,
                            i.toString() + " : " + childrenResult[i].name + " : " + childrenResult[i].isSelected
                        )


//                     selectedLocationType = multipleItemSelectionSpinner.selectedItem
                        selectChildId =
                            binding.multipleItemSelectionSpinner.selectedIds.joinToString(",")

//                    val str: String = java.lang.String.join(",", selectChildId as String)

//                    Log.e(TAG,"selected item, list ===========$selectedLocationType")
                        Log.e(TAG, "selected item, list ===========$selectChildId")
                        Log.e(TAG, "selected item, list string ===========$selectChildId")
                        Log.e(TAG, " child id======${childrenResult[i].id}")
                        Log.e(TAG, " counter=before=====${counter}")


                        counter = counter + 1
                        val id = SharedPrefUserData(this@ParentBookActivity).getSavedData().id
                        val token = SharedPrefUserData(this@ParentBookActivity).getSavedData().token
                        controller.callChildInfoAPI(id!!, token!!, selectChildId)
                        binding.llTotalPrice.isVisible = true
                        binding.txtBook.isVisible = true


                        finalfees = fees.toInt() * counter


                        Log.e("finalFees1", finalfees.toString());


                        binding.txtFess.text = finalfees.toString()
                        binding.lrPricelayout.visibility=View.VISIBLE
                    }
                    else { // This block is called when a checkbox is deselected
                        // Check if the current child is deselected
                        Log.e("child2", "child is deselected");
                        // The reason this block may not be called on checkbox deselection is likely due to how the checkbox's listener or the logic for handling selection/deselection is implemented.
                        // Typically, the code inside this 'else' block should be triggered when a checkbox is unchecked (i.e., deselected).
                        // However, if the 'isSelected' property of 'childrenResult[i]' is not being updated correctly before this check,
                        // or if the event/callback is not firing as expected on deselection, this block will not execute.
                        // 
                        // To debug, ensure that:
                        // 1. The checkbox's listener is properly set up to handle both selection and deselection events.
                        // 2. The 'isSelected' property is updated immediately when the checkbox state changes, before this logic runs.
                        // 3. Add logs in the checkbox's onCheckedChanged/onClick listener to verify the flow.
                        // 
                        // Example debug log:
                        Log.d(TAG, "Checkbox deselected for child: ${childrenResult[i].name}, isSelected: ${childrenResult[i].isSelected}")

                        if (!childrenResult[i].isSelected) {
//                            counter = counter - 1 // Decrement counter if needed
//                            if (finalfees > 0) { // Ensure finalfees is positive before subtraction
//                                finalfees -= fees.toInt() // Subtract fees for the deselected child
//                            }
//                            Log.e(TAG, "counter=after_deselect1=====${counter}") // Log counter value

                              finalfees=fees.toInt() * counter // Alternative calculation for finalfees


                            binding.txtFess.text = finalfees.toString() // Update the UI with the new finalfees

                            binding.lrPricelayout.visibility=View.VISIBLE

                        }
                        else { // This else block would be reached if childrenResult[i].isSelected is true,
                                 // which contradicts the outer else condition. This part might need review.
                            Log.e(TAG, "counter=after_deselect2=====${counter}") // Log counter value
                        }
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

        // The controller.callChildInfoAPI call within the loop in setItems callback of multipleItemSelectionSpinner will trigger this method.
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