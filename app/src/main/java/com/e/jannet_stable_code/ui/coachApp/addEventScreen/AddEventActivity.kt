package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.setPadding
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.NothingSelectedSpinnerAdapter
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.coachsportslistdata.CoachSportsListResult
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.gradlistdata.GradeListResult
import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.retrofit.minmaxagedata.MinMaxAgeResult
import com.e.jannet_stable_code.retrofit.response.GetChildGroupListResponse
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.AddNewLocationActivity
import com.e.jannet_stable_code.utils.*
import com.e.jannet_stable_code.viewinterface.ICoachSportsListVIew
import com.e.jannet_stable_code.viewinterface.IGetGradeListView
import com.e.jannet_stable_code.viewinterface.ILocationView
import com.e.jannet_stable_code.viewinterface.IMinMAxAgeView
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.topbar_layout.*


class AddEventActivity : BaseActivity(), ILocationView, ICoachSportsListVIew, IGetGradeListView,
    IMinMAxAgeView {
    var childAgeList: List<GetChildGroupListResponse.Result?>? = null
    var selectedChildAgeGroupId = ""
    private var addEventViewModel: AddEventViewModel? = null
    private var addEventObject: AddEventObject? = null
    private var imagePickFlag = 0
    private var pickImage: PickImage? = null
    private var imgString1 = ""
    private var imgString2 = ""
    private var imgString3 = ""
    private var imgString4 = ""
    private var imgString5 = ""
    private var imgString6 = ""
    private var sportsIdsSelected = ""
    var sportsListTop = ArrayList<SportsListResponse.Result>()
    var sportsListStings = ArrayList<String>()
    lateinit var locationController: ILocationController
    var locationId = "0"
    var selectedgradeID = "0"
    var selectedMaxgradeID = "0"
    var minRangeSelected: Int = 0
    var maxRangeSelected: Int = 0
    var selectedSportList = ""
    lateinit var getSportController: IGetSportController
    lateinit var coachSportsListCOntroller: ICoachSportsListController
    lateinit var gradeController: IGetGradeListController

    lateinit var minMaxAgeController: IMinMaxAgeController
    val tempmaxAgeList = ArrayList<String?>()
    var temparraylist = ArrayList<String>()

    var minAge = 1
    var maxAge = 100
    private var minMaxgeResult: List<MinMaxAgeResult?>? = null
    private var gradeListResult: List<GradeListResult?>? = null
    lateinit var adapterMaxRange: ArrayAdapter<String>
    private var tempGradeMaxList: ArrayList<GradeListResult?>? = null

    override fun getController(): IBaseController? {

        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        // location spnner

        imgGrade.setImageResource(R.mipmap.rad)

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

//        gradeController = GetGradeListController(this,this)
//        gradeController.callGetGradeListApi(id,token)
        minMaxAgeController = MinMaxAgeController(this, this)
        locationController = LocationController(this, this)
        locationController.callLocationApi(id, token)
        Log.e(TAG, "=====$id=======$token")
        txtlocation.text = "Select Location"


//        coachSportsListCOntroller = CoachSportsListController(this,this)
//        coachSportsListCOntroller.callCoachSportsListApi(id,token,id)
////
        showLoader()

//        getSportController = GetSportsController(this, this)
//        getSportController.callGetSportsApi(id, token)

        btn_add_location.setOnClickListener {

            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Add Location")
                .setMessage("You Wan to Add New Location?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        var intent = Intent(this, AddNewLocationActivity::class.java)
                        startActivity(intent)
                    })
                .setNegativeButton("No", null)
                .show()

        }


        addEventObject = AddEventObject()
        addEventViewModel = AddEventViewModel(this)
        setTopBar()

        img1.setOnClickListener {
            imagePickFlag = 1
            pickImage = PickImage(this@AddEventActivity)
        }
        img2.setOnClickListener {
            imagePickFlag = 2
            pickImage = PickImage(this@AddEventActivity)
        }
        img3.setOnClickListener {
            imagePickFlag = 3
            pickImage = PickImage(this@AddEventActivity)
        }
        img4.setOnClickListener {
            imagePickFlag = 4
            pickImage = PickImage(this@AddEventActivity)
        }
        img5.setOnClickListener {
            imagePickFlag = 5
            pickImage = PickImage(this@AddEventActivity)
        }
        img6.setOnClickListener {
            imagePickFlag = 6
            pickImage = PickImage(this@AddEventActivity)
        }
        txtDate.setOnClickListener {
            datePicker(
                txtDate, this, this.supportFragmentManager,
                object : DatePickerResult {
                    override fun onSuccess(string: String) {

                    }
                }, false, futureDatesOnly = true
            )
        }
        txtAdd.setOnClickListener {


            val allData = AddEventObject()
            allData.img1 = imgString1
            allData.img2 = imgString2
            allData.img3 = imgString3
            allData.img4 = imgString4
            allData.img5 = imgString5
            allData.img6 = imgString6
            allData.event_name = etxtEventName.text.toString().trim()
            allData.description = etxtEventDiscription.text.toString()

            allData.fees = etxtFees.text.toString().trim()
            allData.location = locationId.toString()
            allData.sportTypes = selectedSportList.trim()
            allData.coach_id = id.toString()

            allData.date = txtDate.text.toString().trim()

            allData.noParticipants = etxtNoParticipants.text.toString().trim()
            allData.gender_applicable = txtSelectedApplicableGender.text.toString()
            allData.grade_id = selectedgradeID.toString().trim()
            Log.e("TAG", "onCreate: selected grade id is ==$selectedgradeID")
            allData.minimum_age = minRangeSelected.toString()
            allData.max_age = maxRangeSelected.toString()
            allData.min_grade = selectedgradeID.toString()
            allData.max_grade = selectedMaxgradeID.toString()

            if (addEventViewModel!!.checkValidData(allData)) {

                if (etxtEventName.text == null) {

                    Toast.makeText(this, "Please Enter Event Name", Toast.LENGTH_LONG).show()
                } else if (etxtEventDiscription.text == null) {

                    Toast.makeText(this, "Please Enter Event Description", Toast.LENGTH_LONG).show()


                } else if (txtSelectedApplicableGender.text.trim()
                        .toString() == "Select Applicable Gender"
                ) {

                    showToast("Selcet Applicable Gender")
                } else if (txtlocation.text.trim().toString() == "Select Location") {

                    showToast("Select Location")
                } else if (txtSportscoach.text.trim().toString() == "Select Sports") {

                    showToast("Select Sports ")
                } else if (txtSelectedGrade.text.trim().toString() == "Select Grade") {

                    showToast("Select Grade")
                } else if (txtSelectedGradeMax.text.trim().toString() == "Select Maximum Grade") {

                    showToast("Select Maximum Grade")

                } else {

                    addEventViewModel!!.callAddEventApi()
                }
            }

        }

        gradeAgeRangeFunction()

        initiateApiCalls()

        txtSportscoach.setOnClickListener {

            multispinner.performClick()
        }

        txtlocation.setOnClickListener {

            spinnerLocation.performClick()

        }

        txtSelectedApplicableGender.setOnClickListener {


            spinnerGender.performClick()

        }


        txtSelectedGrade.setOnClickListener {
            spinnerSelectGrade.performClick()
        }
        txtSelectedGradeMax.setOnClickListener {

            spinnerSelectGradeMax.performClick()
            if (txtSelectedGrade.text.trim().toString() == "Select Grade") {

                showToast("Select Minimum Grade First")
            } else {

            }
        }

        txtSelectMaxRange.setOnClickListener {
            spinnerSelectMaxRange.performClick()
        }

        txtSelectMinRange.setOnClickListener {
            spinnerSelectMinRange.performClick()
        }
    }

    private fun AgerangeFunction() {
        if (minMaxgeResult!!.isNotEmpty()) {


            val tempminAgeList = ArrayList<String?>()

            for (i in minMaxgeResult!![0]!!.getMinAgeStart()!!
                .toInt()..minMaxgeResult!![0]!!.getMinAgeEnd()!!
                .toInt()) {

                tempminAgeList.add("$i")

            }

            Log.e(TAG, "onMinMaxAgeSuccess: minmimum age list $tempminAgeList")

            val adapterMinRange: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    tempminAgeList
                )
            adapterMinRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSelectMinRange.adapter = adapterMinRange
            spinnerSelectMinRange.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        try {
                            txtSelectMinRange.text = tempminAgeList[position]
                            val minimumselectedAge = tempminAgeList[position]

                            temparraylist = ArrayList<String>()
                            for (i in minimumselectedAge?.toInt()
                                ?.plus(1)!!..minMaxgeResult!![0]!!.getMaxAgeEnd()
                                ?.toInt()!!) {

                                tempmaxAgeList.add("$i")
                                temparraylist.add("$i")



                                Log.e(
                                    TAG,
                                    "onMinMaxAgeSuccess: after mimimum age temp array listselection $temparraylist  "
                                )

                                Log.e(
                                    TAG,
                                    "onMinMaxAgeSuccess:tempmap age list  minimiiiii $tempmaxAgeList"
                                )

                            }


//                            if (txtSelectMaxRange.text.toString() == "") {
//                                txtSelectMinRange.text
//
//                            //"Select Minimum Age"
//                            } else {
//                                val minRangeSelected: Int = tempminAgeList[position]?.toInt()!!
//                                val maxRangeSelected: Int = txtSelectMaxRange.text.toString().toInt()
//                                if (minRangeSelected > maxRangeSelected) {
//                                    Utilities.showToast(
//                                        this@AddEventActivity,
//                                        "Minimum age cant be more than Maximum age."
//                                    )
//                                } else
//                                {
//                                    txtSelectMinRange.text = tempminAgeList[position]
//                                    val minimumselectedAge = tempminAgeList[position]
//
//                                    Log.e(
//                                        TAG,
//                                        "onMinMaxAgeSuccess: after mimimum selected age age selection $minimumselectedAge  "
//                                    )
//
//                                    val temparraylist = ArrayList<String>()
//                                    for (i in minimumselectedAge?.toInt()!!..minMaxgeResult!![0]!!.getMaxAgeEnd()
//                                        ?.toInt()!!) {
//
//
//                                        tempmaxAgeList.add("$i")
//                                        temparraylist.add("$i")
//
//
//
//                                        Log.e(
//                                            TAG,
//                                            "onMinMaxAgeSuccess: after mimimum age temp array listselection $temparraylist  "
//                                        )
//                                    }
//
//                                }
//                            }

//maxagelist insied minimum item selection
                            val adapterMaxRange = ArrayAdapter<String>(
                                applicationContext,
                                android.R.layout.simple_spinner_item,
                                temparraylist
                            )
                            Log.e(TAG, "onMinMaxAgeSuccess:tempmap age list   $temparraylist")
                            adapterMaxRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerSelectMaxRange.adapter = adapterMaxRange
                            spinnerSelectMaxRange.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parentView: AdapterView<*>?,
                                        selectedItemView: View?,
                                        position: Int,
                                        id: Long,
                                    ) {
                                        try {
//                            if (firstTimeMaxSelected) {
//                                txtSelectMaxRange.text = tempmaxAgeList[(tempmaxAgeList.size - 1)]
//                                firstTimeMaxSelected = false
//                            } else {
//                                minRangeSelected = txtSelectMinRange.text.toString().toInt()
//                                maxRangeSelected = tempmaxAgeList[position]!!.toInt()
//                                if (maxRangeSelected < minRangeSelected) {
//                                    Utilities.showToast(
//                                        this@AddEventActivity,
//                                        "Maximum age cant be less than Minimum age."
//                                    )
//                                } else {
//                                    txtSelectMaxRange.text = tempmaxAgeList[position]
//                                }
//                            }

                                            txtSelectMaxRange.text = temparraylist[position]

                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            txtSelectMaxRange.text = ""
                                        }
                                    }

                                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                                        txtSelectMaxRange.text = ""
                                    }
                                }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            txtSelectMinRange.text = ""
                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                        txtSelectMinRange.text = ""
                    }
                }


            //select max range

            var minmum_age = txtSelectMinRange.text.toString()


            for (i in minMaxgeResult!![0]!!.getMaxAgeStart()!!
                .toInt()..minMaxgeResult!![0]!!.getMaxAgeEnd()!!
                .toInt()) {

//                tempmaxAgeList.add("$i")
//            if (minmum_age.trim() == "Select Minimum Age") {
//
//                minmum_age = "0"
//                if (minmum_age.toInt() < i) {
//
//                    tempmaxAgeList.add("$i")
//                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age ceck if $tempmaxAgeList  ")
//
//                } else {
//                    tempmaxAgeList.add("$i")
//                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age  not check if   $tempmaxAgeList")
//
//
//                }
//            } else {
//
//
//                if (minmum_age.toInt() < i) {
//
//                    tempmaxAgeList.add("$i")
//                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age ceck else $tempmaxAgeList  ")
//
//                } else {
//                    tempmaxAgeList.add("$i")
//                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age  not check else  $tempmaxAgeList")
//
//
//                }
//            }

            }

            Log.e(TAG, "onMinMaxAgeSuccess: maximum age list $tempmaxAgeList")

//            val adapterMaxRange: ArrayAdapter<String> =
//                ArrayAdapter<String>(
//                    applicationContext,
//                    android.R.layout.simple_spinner_item,
//                    tempmaxAgeList
//                )

        } else {

            //
        }

    }

    private fun gradeAgeRangeFunction() {
        imgGrade.setOnClickListener {
            selectType(1)
        }
        txtGrade1.setOnClickListener {
            imgGrade.performClick()
        }
        imgAge.setOnClickListener {
            selectType(2)
        }
        txtAge.setOnClickListener {
            imgAge.performClick()
        }

        val aplicableList = resources.getStringArray(R.array.ApplicableGender)

        val adapterApplicableGender: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_item,
            aplicableList
        )
        adapterApplicableGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapterApplicableGender
        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + "" + aplicableList[position],
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.e(TAG, "onItemSelected:name  " + "${aplicableList[position]}")

                val name = aplicableList[position]

                txtSelectedApplicableGender.text = name.toString()
                Log.e(TAG, "onItemSelected: applicable gender elected list ===$name")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }


//        var tempMinTypeList = ArrayList<String?>()
//        tempMinTypeList.add(0,"Select Minimum Age")
//        tempMinTypeList.addAll(ageList)
//
//
//        var tempMaxTypeList = ArrayList<String?>()
//        tempMaxTypeList.add(0,"Select Maximum Age")
//        tempMaxTypeList.addAll(ageList)
//

        //       txtSelectMaxRange.setOnClickListener {
//            spinnerSelectMaxRange.performClick()
//        }


        //minimmmax age


    }

    var firstTimeMaxSelected = true
    var selectedType = 0
    private fun selectType(i: Int) {
        selectedType = i
        if (i == 1) {
            //Grade
            imgGrade.setImageResource(R.mipmap.rad)
            imgAge.setImageResource(R.mipmap.rad1)
            llGradeMain.visibility = View.VISIBLE
            llGradeMainMax.visibility = View.VISIBLE
            llMinmumAgeRange.visibility = View.GONE
            llMaxAgeRange.visibility = View.GONE
            minRangeSelected = 0
            maxRangeSelected = 0
        } else if (i == 2) {
            //Age
            imgGrade.setImageResource(R.mipmap.rad1)
            imgAge.setImageResource(R.mipmap.rad)
            llGradeMain.visibility = View.GONE
            llGradeMainMax.visibility = View.GONE
            llMinmumAgeRange.visibility = View.VISIBLE
            llMaxAgeRange.visibility = View.VISIBLE
            selectedgradeID = "0"
        }
    }

    private fun initiateApiCalls() {
        var temp: DoneInterFace? = null
        val listner = object : DoneInterFace {
            override fun done(str: String) {
                if (str == "getChildList") getSportsList(temp!!)
            }
        }

        temp = listner
//        getChildList(listner)


    }

    private fun getSportsList(listner: DoneInterFace) {
        GetSportsListController(this, object : ControllerInterface {
            override fun onFail(error: String?) {
            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as SportsListResponse
                    setSportsList(data.getResult()!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                listner.done("getSportsList")
            }

        }).callApi()
    }

    fun setSportsList(sportsList: List<SportsListResponse.Result?>) {
        for (i in sportsList.indices) {
            try {
                sportsListTop.add(sportsList[i]!!)
                sportsListStings.add(sportsList[i]!!.sportsName!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        txtSports.setOnClickListener { multispinner.performClick() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) {
            when (imagePickFlag) {
                1 -> {
                    img1.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img1)
                    imgString1 = pickImage!!.getImage()!!
                }
                2 -> {
                    img2.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img2)
                    imgString2 = pickImage!!.getImage()!!
                }
                3 -> {
                    img3.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img3)
                    imgString3 = pickImage!!.getImage()!!
                }
                4 -> {
                    img4.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img4)
                    imgString4 = pickImage!!.getImage()!!
                }
                5 -> {
                    img5.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img5)
                    imgString5 = pickImage!!.getImage()!!
                }
                6 -> {
                    img6.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img6)
                    imgString6 = pickImage!!.getImage()!!
                }
            }
        }
    }

    private fun setChildList(result: List<GetChildGroupListResponse.Result?>) {
        val strArray = ArrayList<String>()
        childAgeList = result
        for (i in result.indices) {
            try {
                strArray.add(result[i]!!.name!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, strArray
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.prompt = "Select child group"

        spinner.adapter = NothingSelectedSpinnerAdapter(
            adapter,
            R.layout.contact_spinner_row_nothing_selected,  // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
            this
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                try {
                    txtChildGroup.text = childAgeList!![position - 1]!!.name
                    selectedChildAgeGroupId = childAgeList!![position - 1]!!.id.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    txtChildGroup.text = ""
                    selectedChildAgeGroupId = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                txtChildGroup.text = ""
                selectedChildAgeGroupId = ""
            }
        }
        txtChildGroup.setOnClickListener { spinner.performClick() }
    }

    private fun setTopBar() {
        txtTitle.text = resources.getString(R.string.add_event)
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onLocationListSuccess(response: List<LocationResult?>?) {

        if (response != null) {


            hideLoader()

            var tempLocationTypeList = ArrayList<LocationResult?>()
            var lisBtHint = LocationResult()
            lisBtHint.setName("Select Location")
            tempLocationTypeList.add(0, lisBtHint)
            tempLocationTypeList.addAll(response)

            val adapterLoction =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, tempLocationTypeList!!)
            adapterLoction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerLocation.adapter = adapterLoction
            spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long,
                ) {
                    try {

                        var selectedSprefrenceItem = spinnerLocation.selectedItem as LocationResult
                        Log.d(
                            TAG,
                            "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getName()
                        )

                        locationId = selectedSprefrenceItem.getId().toString()
                        txtlocation.text = selectedSprefrenceItem.getName().toString()

                    } catch (e: Exception) {
                        e.printStackTrace()
                        txtlocation.text = ""

                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                    txtlocation.text = "Select Location"
                }
            }
        } else {
            hideLoader()
        }

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        showLoader()
        gradeController = GetGradeListController(this, this)
        gradeController.callGetGradeListApi(id, token)


    }

    override fun onCoatListSuccess(response: List<Coat?>?) {
        hideLoader()
    }


    override fun onGetSportsListSuccess(response: List<CoachSportsListResult?>?) {

        //start spots list
        hideLoader()

        var tempSportsTypeList = ArrayList<CoachSportsListResult?>()
        var lisBtHint = CoachSportsListResult()
        lisBtHint.setSportsName("Select Sports")
        tempSportsTypeList.add(0, lisBtHint)
        tempSportsTypeList.addAll(response!!)

        val adapterSports =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempSportsTypeList!!)
        adapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        multispinner.adapter = adapterSports
        multispinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                try {

                    var selectedSprefrenceItem = multispinner.selectedItem as CoachSportsListResult
                    Log.d(
                        TAG,
                        "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getSportsName()
                    )

                    selectedSportList = selectedSprefrenceItem.getId().toString()
                    txtSportscoach.text = selectedSprefrenceItem.getSportsName().toString()
                    Log.e(TAG, "onItemSelected: ")


                } catch (e: Exception) {
                    e.printStackTrace()

                    txtSportscoach.text = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                txtSportscoach.text = "Select Sports"
            }
        }

        val stordata = StoreUserData(this)
        val id = stordata.getString(Constants.COACH_ID)
        val token = stordata.getString(Constants.COACH_TOKEN)
        minMaxAgeController.callMinMaxAgeApi(id, token)
        showLoader()

    }

    override fun onGradeListSuccess(gradeList: List<GradeListResult?>?) {


        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        if (gradeList != null) {

            gradeListResult = gradeList
            getGradeListData()

        } else {
            hideLoader()
        }

        showLoader()
        coachSportsListCOntroller = CoachSportsListController(this, this)
        coachSportsListCOntroller.callCoachSportsListApi(id, token, id)
//

    }

    private fun getGradeListData() {

        var tempGradeTypeList = ArrayList<GradeListResult?>()
        var lisBtHint = GradeListResult()
        lisBtHint.setName("Select Grade")
        tempGradeTypeList.add(0, lisBtHint)
        tempGradeTypeList.addAll(gradeListResult!!)

        hideLoader()
        val adapterGrade =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempGradeTypeList!!)
        adapterGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        tempGradeMaxList = ArrayList<GradeListResult?>()

        spinnerSelectGrade.adapter = adapterGrade
        spinnerSelectGrade.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long,
                ) {
                    try {

                        tempGradeMaxList!!.clear()
                        var selectedGradeItem =
                            spinnerSelectGrade.selectedItem as GradeListResult
                        selectedgradeID = selectedGradeItem.getId().toString()
                        Log.d(
                            TAG,
                            "onItemSelected: " + selectedGradeItem.getId() + "  " + selectedGradeItem.getName()
                        )
                        txtSelectedGrade.text = selectedGradeItem.getName().toString()


                        gradeListResult!!.forEach {

                            if (it != null) {
                                if (selectedGradeItem.getId()!! < it.getId()!!) {

                                    tempGradeMaxList!!.add(it)
                                }
                                Log.d(TAG, "onItemSelected: tempgrade mac list $tempGradeMaxList")

                            }
                        }

                        //grade max                                //max grade list
                        //max adapter

                        val adapteMaxrGrade = ArrayAdapter(
                            this@AddEventActivity,
                            android.R.layout.simple_spinner_item,
                            tempGradeMaxList!!
                        )
                        adapteMaxrGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        spinnerSelectGradeMax.adapter = adapteMaxrGrade
                        spinnerSelectGradeMax.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parentView: AdapterView<*>?,
                                    selectedItemView: View?,
                                    position: Int,
                                    id: Long,
                                ) {

                                    try {

                                        var selectedGradeItem =
                                            spinnerSelectGradeMax.selectedItem as GradeListResult
                                        selectedMaxgradeID = selectedGradeItem.getId().toString()
                                        Log.d(
                                            TAG,
                                            "onItemSelected: max grade " + selectedGradeItem.getId() + "  " + selectedGradeItem.getName()
                                        )
                                        txtSelectedGradeMax.text =
                                            selectedGradeItem.getName().toString()

                                        Log.d(
                                            TAG,
                                            "onItemSelectedd  after select max from drop down ${txtSelectedGradeMax.text}"
                                        )

                                    } catch (e: Exception) {
                                        e.printStackTrace()


                                        Log.e(
                                            TAG, "onItemSelectedd exception $e"
                                        )
                                    }
                                }

                                override fun onNothingSelected(parentView: AdapterView<*>?) {

                                    showToast("N")
                                }
                            }
                    } catch (e: Exception) {
                        e.printStackTrace()

                    }


                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }

        //


    }

    override fun onMinMaxAgeSuccess(response: List<MinMaxAgeResult?>?) {

//        val ageList = ArrayList<String>()
//        for (i in minAge..maxAge) {
//            ageList.add("$i")
//        }

        minMaxgeResult = response!!
        AgerangeFunction()


    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
    }

    interface DoneInterFace {
        fun done(str: String)
    }

    class AddEventObject {
        var img1 = ""
        var img2 = ""
        var img3 = ""
        var img4 = ""
        var img5 = ""
        var img6 = ""
        var imgCount = ""

        //    var name = ""
        var fees = ""
        var location = ""
        var sportTypes = ""
        var date = ""
        var noParticipants = ""
        var childGroup = ""
        var event_name = ""
        var description = ""
        var gender_applicable = ""
        var grade_id = ""
        var minimum_age = ""
        var max_age = ""
        var coach_id = ""
        var min_grade = ""
        var max_grade = ""

    }

    class ChooseApplicableGender {

        private var id: Int? = null

        private var applicablegenderName: String? = null

        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }

        fun getApplicableGenderName(): String? {
            return applicablegenderName
        }

        fun setApplicableGenderName(applicablegenderName: String?) {
            this.applicablegenderName = applicablegenderName
        }

        override fun toString(): String {
            return "$applicablegenderName"
        }

    }
}