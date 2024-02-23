package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.coachsportslistdata.CoachSportsListResult
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.gradlistdata.GradeListResult
import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult
import com.e.jannet_stable_code.retrofit.response.EventDetailResponse
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.AddImageViewModel
import com.e.jannet_stable_code.ui.coachApp.AddNewLocationActivity
import com.e.jannet_stable_code.utils.*
import com.e.jannet_stable_code.viewinterface.*
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_edit_event.*
import kotlinx.android.synthetic.main.item_add_child_list.*
import kotlinx.android.synthetic.main.topbar_layout.*
import java.util.*

//IGetSportView
class EditEventActivity : BaseActivity(),IGetGradeListView, ILocationView,
    ICoachSportsListVIew {

    private var editEventObject: EditEventObject? = null
    private var addImageObject:AddImageObject?=null
    private var addImageViewmodel :AddImageViewModel?=null
    private var editEventViewModel: EditEventViewModel? = null
    private var imagePickFlag = 0
    private var pickImage: PickImage? = null
    private var imgString1 = ""
    private var imgString2 = ""
    private var imgString3 = ""
    private var imgString4 = ""
    private var imgString5 = ""
    private var imgString6 = ""

    private var image1id = "0"
    private var image1 = ""
    private var image2id = "0"
    private var image2 = ""

    private var image3id = "0"
    private var image3 = ""
    private var image4id = "0"
    private var image4 = ""

    private var image5id = "0"
    private var image5 = ""
    private var mainImage = ""
    var sportsListTop = ArrayList<SportsListResponse.Result>()
    var sportsListStings = ArrayList<String>()
    lateinit var locationController: ILocationController
    var locationId = "0"
    var selectedgradeID = "0"
    var minRangeSelected: Int = 0
    var maxRangeSelected: Int = 0
    var selectedSportList = ""
    lateinit var getSportController: IGetSportController
    lateinit var coachSportsListCOntroller: ICoachSportsListController
        lateinit var gradeController:IGetGradeListController
    var minAge = 1
    var maxAge = 100


    override fun getController(): IBaseController? {
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        setTopBar()
        imgGrade_edit.setImageResource(R.mipmap.rad)
        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        showLoader()

        locationController = LocationController(this, this)
        locationController.callLocationApi(id, token)

        btn_add_location_edit.setOnClickListener {

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

        txtDate_edit.setOnClickListener {
            datePicker(
                txtDate_edit, this, this.supportFragmentManager,
                object : DatePickerResult {
                    override fun onSuccess(string: String) {

                    }
                }, false, futureDatesOnly = true
            )
        }

        val eventID = intent.getStringExtra("EVENT_ID")
        var eventDetailResponse = intent.getSerializableExtra("EVENT_RESULT")

        Log.d("TAG", "onCreate: test447>>" + intent.getStringExtra("EVENT_ID"))
        if (intent.getStringExtra("EVENT_ID") != null &&
            !intent.getStringExtra("EVENT_ID").equals("null") &&
            !intent.getStringExtra("EVENT_ID").equals("")
        ) {
            EventDetailController(this@EditEventActivity, object : ControllerInterface {
                override fun onFail(error: String?) {

                    Log.e("TAG", "onFail: fail ==========$error")
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun <T> onSuccess(response: T, method: String) {
                    try {

                        val resp = response as EventDetailResponse


                        val address = resp.getResult()!![0]!!.getAddress().toString()
                        val event_name = resp.getResult()!![0]!!.getEventName().toString()
                        val description = resp.getResult()!![0]!!.getDescription().toString()
                        selectedSportList =resp.getResult()!![0]!!.getSportsId().toString()
                        txtSelectedApplicableGender_edit.hint =
                            resp.getResult()!![0]!!.getGenderApplicable().toString()
                        etxtEventName_edit.hint = resp.getResult()!![0]!!.getEventName().toString()
                        etxtEventDiscription_edit.hint =
                            resp.getResult()!![0]!!.getDescription().toString()
                        etxtFees_edit.hint = resp.getResult()!![0]!!.getFees().toString()
                        txtDate_edit.hint = resp.getResult()!![0]!!.getEventDate().toString()
                        etxtNoParticipants_edit.hint =
                            resp.getResult()!![0]!!.getParticipants().toString()
                        txtlocation_edit.hint = resp.getResult()!![0]!!.getAddress().toString()
                        txtSelectedGrade_edit.hint = resp.getResult()!![0]!!.getGrade().toString()
                        txtSelectMinRange_edit.hint = resp.getResult()!![0]!!.getMinAge().toString()
                        txtSelectMaxRange_edit.hint = resp.getResult()!![0]!!.getMaxAge().toString()
                        var tempList = resp.getResult()!![0]!!.getSportsName()
                        var sportslist = ArrayList<String>()

                        tempList?.forEach {

                            val sporsName = it?.getSportsName()

                            if (sporsName != null) {

                                sportslist.add(sporsName)
                            }

                        }
                        Log.e(TAG, "onSuccess: sportslist on edit event===$sportslist")
                        val listString: String = java.lang.String.join(", ", sportslist)

                        txtSportscoach_edit.text = listString
                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getMainimage())
                            .into(img1_edit)


                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![0]?.getImage())
                            .into(img2_edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![1]?.getImage())
                            .into(img3_edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![2]?.getImage())
                            .into(img4_edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![3]?.getImage())
                            .into(img5_edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![4]?.getImage())
                            .into(img6_edit)

                        if (resp.getResult()!![0]!!.getImages()!![0]?.getId() != null || resp.getResult()!![0]!!.getImages()!![0]?.getId() != 0) {


                            image1id = resp.getResult()!![0]!!.getImages()!![0]?.getId().toString()
                            image1 =
                                resp.getResult()!![0]!!.getImages()?.get(0)?.getImage().toString()
                        }

                        if (resp.getResult()!![0]!!.getImages()!![1]?.getId() != null || resp.getResult()!![0]!!.getImages()!![1]?.getId() != 0) {

                            image2id = resp.getResult()!![0]!!.getImages()!![1]?.getId().toString()
                            image2 =
                                resp.getResult()!![0]!!.getImages()?.get(1)?.getImage().toString()

                        }

                        if (resp.getResult()!![0]!!.getImages()!![2]?.getId() != null || resp.getResult()!![0]!!.getImages()!![2]?.getId() != 0) {

                            image3id = resp.getResult()!![0]!!.getImages()!![2]?.getId().toString()
                            image3 =
                                resp.getResult()!![0]!!.getImages()?.get(2)?.getImage().toString()

                        }

                        if (resp.getResult()!![0]!!.getImages()!![3]?.getId() != null || resp.getResult()!![0]!!.getImages()!![3]?.getId() != 0) {

                            image4id = resp.getResult()!![0]!!.getImages()!![3]?.getId().toString()
                            image4 =
                                resp.getResult()!![0]!!.getImages()?.get(3)?.getImage().toString()
                        }
                        if (resp.getResult()!![0]!!.getImages()!![4]?.getId() != null || resp.getResult()!![0]!!.getImages()!![4]?.getId() != 0) {

                            image5id = resp.getResult()!![0]!!.getImages()!![4]?.getId().toString()
                            image5 =
                                resp.getResult()!![0]!!.getImages()?.get(4)?.getImage().toString()

                        }




                        Log.e("ID", "onSuccess: image pose 1 id $image1id")
                        Log.e("ID", "onSuccess: image pose 2 id $image2id")

                        Log.e("TAG", "onSuccess: event detil success===${resp.getResult()}")

                        Log.e(
                            "EventDetail",
                            "=========main iamge===${
                                resp.getResult()!![0]?.getMainimage().toString()
                            }"
                        )

                        Log.e("TAG", "onSuccess: =========Adrress$address")
                        Log.e(
                            "TAG",
                            "onSuccess: ${resp.getResult()!![0]!!.getImages()!![0]?.getId()}",
                        )
                        Log.e(
                            "TAG",
                            "onSuccess: ${resp.getResult()!![0]!!.getImages()!![1]?.getId()}",
                        )
                        Log.e(
                            "TAG",
                            "onSuccess: ${resp.getResult()!![0]!!.getImages()!![2]?.getId()}",
                        )
                        Log.e(
                            "TAG",
                            "onSuccess: ${resp.getResult()!![0]!!.getImages()!![3]?.getId()}",
                        )
                        Log.e(
                            "TAG",
                            "onSuccess: ${resp.getResult()!![0]!!.getImages()!![4]?.getId()}",
                        )
//                        Log.e(
//                            "TAG",
//                            "onSuccess: ${resp.getResult()!![0]!!.getImages()!![5]?.getId()}",
//                        )


                        Log.e(
                            "EventDetail",
                            "=========images===${resp.getResult()!![0]?.getImages().toString()}"
                        )

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).callApi(intent.getStringExtra("EVENT_ID")!!)
        }


        editEventObject = EditEventActivity.EditEventObject()
        editEventViewModel = EditEventViewModel(this)
        addImageViewmodel = AddImageViewModel(this)
        addImageObject = AddImageObject()




        img1_edit.setOnClickListener {

            imagePickFlag = 1
            pickImage = PickImage(this@EditEventActivity)
        }
        img2_edit.setOnClickListener {

            if (image1id == null) {

                imagePickFlag = 2
                pickImage = PickImage(this@EditEventActivity)

            } else {
                imagePickFlag = 2
                pickImage = PickImage(this@EditEventActivity)

            }

        }
        img3_edit.setOnClickListener {


            imagePickFlag = 3
            pickImage = PickImage(this@EditEventActivity)
        }
        img4_edit.setOnClickListener {


            imagePickFlag = 4
            pickImage = PickImage(this@EditEventActivity)
        }
        img5_edit.setOnClickListener {


            imagePickFlag = 5
            pickImage = PickImage(this@EditEventActivity)
        }
        img6_edit.setOnClickListener {


            imagePickFlag = 6
            pickImage = PickImage(this@EditEventActivity)
        }


        txtAdd_edit.setOnClickListener {

            val eventID = intent.getStringExtra("EVENT_ID")

            val allData = EditEventObject()
            val imageData = AddImageObject()

            imageData.event_id = eventID.toString()

            if (image1id.trim()=="0"){
                imageData.img2 = imgString2

            }

            if (image2id.trim()=="0"){

                imageData.img3=imgString3
            }

            if (image3id.trim()=="0"){

                imageData.img4=imgString3
            }

            if (image4id.trim().toString()=="0"){

                imageData.img5=imgString4

            }

            if (image5id.trim().toString()=="0"){

                imageData.img6=imgString5

            }

            allData.event_id = eventID.toString()
            allData.event_name = etxtEventName_edit.text.toString()
            allData.description = etxtEventDiscription_edit.text.toString()
            allData.fees = etxtFees_edit.text.toString()
            allData.location = locationId.toString()
            allData.sportTypes = selectedSportList.trim()
            allData.date = txtDate_edit.text.toString().trim()
            allData.noParticipants = etxtNoParticipants_edit.text.toString()
            allData.gender_applicable = "m/f"
            allData.grade_id = selectedgradeID.toString().trim()
            allData.minimum_age = minRangeSelected.toString()
            allData.max_age = maxRangeSelected.toString()
            allData.imageId1 = image1id
            Log.e("TAG", "imag1 id =====$image1id: ")
            allData.imageId2 = image2id
            allData.imageId3 = image3id
            allData.imageId4 = image4id
            allData.imageId5 = image5id
            allData.img1 = imgString1
            allData.img2 = imgString2
            allData.img3 = imgString3
            allData.img4 = imgString4
            allData.img5 = imgString5
            allData.img6 = imgString6


            if (editEventViewModel!!.checkValidData(allData)) {


                editEventViewModel!!.callEditEventApi()
//                }
            }

            if (addImageViewmodel!!.checkValidData(imageData)){


                addImageViewmodel!!.callAddmageApi()
            }

        }

        gradeAgeRangeFunction()

        initiateApiCalls()

        txtSportscoach_edit.setOnClickListener {

            multispinner_edit.performClick()
            showToast("Sports Field")
        }

    }

    private fun gradeAgeRangeFunction() {
        imgGrade_edit.setOnClickListener {
            selectType(1)
        }
        txtGrade1_edit.setOnClickListener {
            imgGrade_edit.performClick()
        }
        imgAge_edit.setOnClickListener {
            selectType(2)
        }
        txtAge_edit.setOnClickListener {
            imgAge_edit.performClick()
        }

        val aplicableList = resources.getStringArray(R.array.ApplicableGender)

        val adapterApplicableGender: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_item,
            aplicableList
        )
        adapterApplicableGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender_edit.adapter = adapterApplicableGender
        spinnerGender_edit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                Log.e(TAG, "onItemSelected:name  " + "${aplicableList[position]}")

                val name = aplicableList[position]

                txtSelectedApplicableGender_edit.text = name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

        val ageList = ArrayList<String>()
        for (i in minAge..maxAge) {
            ageList.add("$i")
        }

        val adapterMinRange: ArrayAdapter<String> =
            ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_item,
                ageList
            )
        adapterMinRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectMinRange_edit.adapter = adapterMinRange
        spinnerSelectMinRange_edit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    try {
                        if (txtSelectMaxRange_edit.text.toString() == "") {
                            txtSelectMinRange_edit.text = ageList[0]
                        } else {
                            val minRangeSelected: Int = ageList[position].toInt()
                            val maxRangeSelected: Int =
                                txtSelectMaxRange_edit.text.toString().toInt()
                            if (minRangeSelected > maxRangeSelected) {
                                Utilities.showToast(
                                    this@EditEventActivity,
                                    "Minimum age cant be more than Maximum age."
                                )
                            } else {
                                txtSelectMinRange_edit.text = ageList[position]
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        txtSelectMinRange_edit.text = ""
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    txtSelectMinRange_edit.text = ""
                }
            }
        txtSelectMinRange_edit.setOnClickListener {
            spinnerSelectMinRange_edit.performClick()
        }

        val adapterMaxRange: ArrayAdapter<String> =
            ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_item,
                ageList
            )
        adapterMaxRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectMaxRange_edit.adapter = adapterMaxRange
        spinnerSelectMaxRange_edit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    try {
                        if (firstTimeMaxSelected) {
                            txtSelectMaxRange_edit.text = ageList[(ageList.size - 1)]
                            firstTimeMaxSelected = false
                        } else {
                            minRangeSelected = txtSelectMinRange_edit.text.toString().toInt()
                            maxRangeSelected = ageList[position].toInt()
                            if (maxRangeSelected < minRangeSelected) {
                                Utilities.showToast(
                                    this@EditEventActivity,
                                    "Maximum age cant be less than Minimum age."
                                )
                            } else {
                                txtSelectMaxRange_edit.text = ageList[position]
                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        txtSelectMaxRange_edit.text = ""
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    txtSelectMaxRange_edit.text = ""
                }
            }
        txtSelectMaxRange_edit.setOnClickListener {
            spinnerSelectMaxRange_edit.performClick()
        }

        txtSelectedGrade_edit.setOnClickListener {
            spinnerSelectGrade_edit.performClick()
        }

        txtlocation_edit.setOnClickListener {

            spinnerLocation_edit.performClick()
        }


        txtSelectedApplicableGender_edit.setOnClickListener {

            spinnerGender_edit.performClick()
        }
//

    }


    var firstTimeMaxSelected = true
    var selectedType = 0


    private fun selectType(i: Int) {
        selectedType = i
        if (i == 1) {
            //Grade
            imgGrade_edit.setImageResource(R.mipmap.rad)
            imgAge_edit.setImageResource(R.mipmap.rad1)
            llGradeMain_edit.visibility = View.VISIBLE
            llMinmumAgeRange_edit.visibility = View.GONE
            llMaxAgeRange_edit.visibility = View.GONE

        } else if (i == 2) {
            //Age
            imgGrade_edit.setImageResource(R.mipmap.rad1)
            imgAge_edit.setImageResource(R.mipmap.rad)
            llGradeMain_edit.visibility = View.GONE
            llMinmumAgeRange_edit.visibility = View.VISIBLE
            llMaxAgeRange_edit.visibility = View.VISIBLE
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

                showToast(error)
                Log.e("sports", "onFail:$error ", )

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
                    img1_edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img1_edit)
                    imgString1 = pickImage!!.getImage()!!
                }
                2 -> {
                    img2_edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img2_edit)
                    imgString2 = pickImage!!.getImage()!!
                }
                3 -> {
                    img3_edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img3_edit)
                    imgString3 = pickImage!!.getImage()!!
                }
                4 -> {
                    img4_edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img4_edit)
                    imgString4 = pickImage!!.getImage()!!
                }
                5 -> {
                    img5_edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img5_edit)
                    imgString5 = pickImage!!.getImage()!!
                }
                6 -> {
                    img6_edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, img6_edit)
                    imgString6 = pickImage!!.getImage()!!
                }
            }
        }
    }

    interface DoneInterFace {
        fun done(str: String)
    }

    class AddImageObject{
        var img2 = ""
        var img3 = ""
        var img4 = ""
        var img5 = ""
        var img6 = ""
        var event_id = ""
        var image_id = ""
        var imgCount = ""


    }

    class EditEventObject {

        var img1 = ""
        var img2 = ""
        var img3 = ""
        var img4 = ""
        var img5 = ""
        var img6 = ""
        var imageId1 = ""
        var imageId2 = ""
        var imageId3 = ""
        var imageId4 = ""
        var imageId5 = ""
        var imageId6 = ""

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
        var event_id = ""
        var image_id = ""
        var imgCount = ""

    }

    private fun setTopBar() {
        txtTitle.text = "EDIT EVENT"
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener {
            onBackPressed()
        }
    }



    override fun onLocationListSuccess(response: List<LocationResult?>?) {


        if (response!=null) {
            hideLoader()
            Log.e(TAG, "onLocationListSuccess: ====${response.toString()}")


            val adapterLoction =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, response!!)
            adapterLoction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerLocation_edit.adapter = adapterLoction
            spinnerLocation_edit.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                    ) {
                        try {

                            var selectedSprefrenceItem =
                                spinnerLocation.selectedItem as LocationResult
                            Log.d(
                                TAG,
                                "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getName()
                            )

                            locationId = selectedSprefrenceItem.getId().toString()
                            txtlocation_edit.text = selectedSprefrenceItem.getName().toString()

                        } catch (e: Exception) {
                            e.printStackTrace()

                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {

                    }
                }

        }else{
            hideLoader()
        }
        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        showLoader()

        coachSportsListCOntroller = CoachSportsListController(this, this)
        coachSportsListCOntroller.callCoachSportsListApi(id, token, id)

    }

    override fun onCoatListSuccess(response: List<Coat?>?) {

                hideLoader()
    }


    override fun onGetSportsListSuccess(response: List<CoachSportsListResult?>?) {

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        if (response!=null) {
            hideLoader()
            val adapterSports = ArrayAdapter(this, android.R.layout.simple_spinner_item, response!!)
            adapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            multispinner_edit.adapter = adapterSports
            multispinner_edit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    try {
//                    txtSportscoach.text = response!![position]?.getSportsName().toString()

                        var selectedSprefrenceItem =
                            multispinner_edit.selectedItem as CoachSportsListResult
                        Log.d(
                            TAG,
                            "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getSportsName()
                        )

                        selectedSportList = selectedSprefrenceItem.getId().toString()
                        txtSportscoach_edit.text = selectedSprefrenceItem.getSportsName().toString()


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                    txtSportscoach_edit.text = "Select Sports"
                }
            }
        }else{
            hideLoader()
        }
        showLoader()
        gradeController = GetGradeListController(this,this)
        gradeController.callGetGradeListApi(id,token)



    }

    override fun onGradeListSuccess(gradeList: List<GradeListResult?>?) {
        hideLoader()
        val adapterGrade = ArrayAdapter(this, android.R.layout.simple_spinner_item, gradeList!!)
        adapterGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSelectGrade_edit.adapter = adapterGrade
        spinnerSelectGrade_edit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                try {

                    var selectedGradeItem = spinnerSelectGrade_edit.selectedItem as GradeListResult
                    selectedgradeID = selectedGradeItem.getId().toString()
                    Log.d(
                        TAG,
                        "onItemSelected: " + selectedGradeItem.getId() + "  " + selectedGradeItem.getName()
                    )

                    txtSelectedGrade_edit.text = selectedGradeItem.getName().toString()

                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
    }


}