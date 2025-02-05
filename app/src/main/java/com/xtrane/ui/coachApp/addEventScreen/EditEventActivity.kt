package com.xtrane.ui.coachApp.addEventScreen

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
import com.xtrane.R
import com.xtrane.databinding.ActivityEditEventBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.coachsportslistdata.CoachSportsListResult
import com.xtrane.retrofit.controller.CoachSportsListController
import com.xtrane.retrofit.controller.EventDetailController
import com.xtrane.retrofit.controller.GetGradeListController
import com.xtrane.retrofit.controller.GetSportsListController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachSportsListController
import com.xtrane.retrofit.controller.IGetGradeListController
import com.xtrane.retrofit.controller.IGetSportController
import com.xtrane.retrofit.controller.ILocationController
import com.xtrane.retrofit.controller.LocationController
import com.xtrane.retrofit.gradlistdata.GradeListResult
import com.xtrane.retrofit.locationdata.Coat
import com.xtrane.retrofit.locationdata.LocationResult
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.retrofit.response.SportsListResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.coachApp.AddImageViewModel
import com.xtrane.ui.coachApp.AddNewLocationActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.DatePickerResult
import com.xtrane.utils.PickImage
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.TAG
import com.xtrane.utils.Utilities
import com.xtrane.utils.datePicker
import com.xtrane.viewinterface.ICoachSportsListVIew
import com.xtrane.viewinterface.IGetGradeListView
import com.xtrane.viewinterface.ILocationView

//IGetSportView
class EditEventActivity : BaseActivity(), IGetGradeListView, ILocationView,
    ICoachSportsListVIew {

    private var editEventObject: EditEventObject? = null
    private var addImageObject: AddImageObject? = null
    private var addImageViewmodel: AddImageViewModel? = null
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
    lateinit var gradeController: IGetGradeListController
    var minAge = 1
    var maxAge = 100

    private lateinit var bind: ActivityEditEventBinding

    override fun getController(): IBaseController? {
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_edit_event)
        bind = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(bind.root)
        Log.e("EditEventActivity", "Welcome: =========")

        setTopBar()

        bind.imgGradeEdit.setImageResource(R.mipmap.rad)


        //  showLoader()



        bind.btnAddLocationEdit.setOnClickListener {

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

        bind.txtDateEdit.setOnClickListener {
            datePicker(
                bind.txtDateEdit, this, this.supportFragmentManager,
                object : DatePickerResult {
                    override fun onSuccess(string: String) {

                    }
                }, false, futureDatesOnly = true
            )
        }

        val eventID = intent.getStringExtra("EVENT_ID")
        var eventDetailResponse = intent.getSerializableExtra("EVENT_RESULT")

        Log.e("TAG", "onCreate: test447>>" + intent.getStringExtra("EVENT_ID"))

        if (intent.getStringExtra("EVENT_ID") != null &&
            !intent.getStringExtra("EVENT_ID").equals("null") &&
            !intent.getStringExtra("EVENT_ID").equals("")
        )
        {
            EventDetailController(this@EditEventActivity, object : ControllerInterface {
                override fun onFail(error: String?) {
                    Log.e("TAG", "onFail: fail ==========$error")
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun <T> onSuccess(response: T, method: String) {
                    try {
                        //  Utilities.dismissProgress()
                        val resp = response as EventDetailResponse
                        Log.e("TAG", "EventDetailResponse: ==========$resp")


                        val address = resp.getResult()!![0]!!.getAddress().toString()
                        val event_name = resp.getResult()!![0]!!.getEventName().toString()

                        Log.e("TAG", "EventDetailResponse:=event_name=$event_name")

                        bind.txtEventNameEdit.text=event_name
                        val description = resp.getResult()!![0]!!.getDescription().toString()
                        bind.txtEventDiscriptionEdit.text=description

                        selectedSportList =resp.getResult()!![0]!!.getSportsId().toString()
                        bind.txtSelectedApplicableGenderEdit.hint =
                            resp.getResult()!![0]!!.getGenderApplicable().toString()
                        bind. etxtEventNameEdit.hint = resp.getResult()!![0]!!.getEventName().toString()
                        bind.etxtEventDiscriptionEdit.hint =
                            resp.getResult()!![0]!!.getDescription().toString()
                        bind.etxtFeesEdit.hint = resp.getResult()!![0]!!.getFees().toString()
                        bind.txtDateEdit.hint = resp.getResult()!![0]!!.getEventDate().toString()
                        bind.etxtNoParticipantsEdit.hint =
                            resp.getResult()!![0]!!.getParticipants().toString()
                        bind.txtlocationEdit.hint = resp.getResult()!![0]!!.getAddress().toString()
                        bind.txtSelectedGradeEdit.hint = resp.getResult()!![0]!!.getGrade().toString()
                        bind.txtSelectMinRangeEdit.hint = resp.getResult()!![0]!!.getMinAge().toString()
                        bind.txtSelectMaxRangeEdit.hint = resp.getResult()!![0]!!.getMaxAge().toString()

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

                        bind.txtSportscoachEdit.text = listString

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getMainimage())
                            .into(bind.img1Edit)


                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![0]?.getImage())
                            .into(bind.img2Edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![1]?.getImage())
                            .into(bind.img3Edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![2]?.getImage())
                            .into(bind.img4Edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![3]?.getImage())
                            .into(bind.img5Edit)

                        Glide.with(this@EditEventActivity)
                            .load(resp.getResult()!![0]!!.getImages()!![4]?.getImage())
                            .into(bind.img6Edit)

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
                            image5 = resp.getResult()!![0]!!.getImages()?.get(4)?.getImage().toString()

                        }


                        Log.e("ID", "onSuccess: image pose 1 id $image1id")
                        Log.e("ID", "onSuccess: image pose 2 id $image2id")
                        Log.e("TAG", "onSuccess: event detil success===${resp.getResult()}")
                        Log.e("EventDetail", "=========main iamge===${resp.getResult()!![0]?.getMainimage().toString()}")

                        //Log.e("TAG", "onSuccess: =========Adrress$address")
                        Log.e("TAG", "onSuccess: ${resp.getResult()!![0]!!.getImages()!![0]?.getId()}",)
                        Log.e("TAG", "onSuccess: ${resp.getResult()!![0]!!.getImages()!![1]?.getId()}",)
                        Log.e("TAG", "onSuccess: ${resp.getResult()!![0]!!.getImages()!![2]?.getId()}",)
                        Log.e("TAG", "onSuccess: ${resp.getResult()!![0]!!.getImages()!![3]?.getId()}",)
                        Log.e("TAG", "onSuccess: ${resp.getResult()!![0]!!.getImages()!![4]?.getId()}",)
                        Log.e("EventDetail", "=========images===${resp.getResult()!![0]?.getImages().toString()}")

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).callApi(intent.getStringExtra("EVENT_ID")!!)
        }



        editEventObject = EditEventObject()
        editEventViewModel = EditEventViewModel(this)
        addImageViewmodel = AddImageViewModel(this)
        addImageObject = AddImageObject()




        bind.img1Edit.setOnClickListener {

            imagePickFlag = 1
            pickImage = PickImage(this@EditEventActivity)
        }
        bind.img2Edit.setOnClickListener {

            if (image1id == null) {

                imagePickFlag = 2
                pickImage = PickImage(this@EditEventActivity)

            } else {
                imagePickFlag = 2
                pickImage = PickImage(this@EditEventActivity)

            }

        }
        bind.img3Edit.setOnClickListener {


            imagePickFlag = 3
            pickImage = PickImage(this@EditEventActivity)
        }
        bind.img4Edit.setOnClickListener {


            imagePickFlag = 4
            pickImage = PickImage(this@EditEventActivity)
        }
        bind.img5Edit.setOnClickListener {


            imagePickFlag = 5
            pickImage = PickImage(this@EditEventActivity)
        }
        bind.img6Edit.setOnClickListener {


            imagePickFlag = 6
            pickImage = PickImage(this@EditEventActivity)
        }


        bind.txtAgeEdit.setOnClickListener {

            val eventID = intent.getStringExtra("EVENT_ID")

            val allData = EditEventObject()
            val imageData = AddImageObject()

            imageData.event_id = eventID.toString()

            if (image1id.trim() == "0") {
                imageData.img2 = imgString2

            }

            if (image2id.trim() == "0") {

                imageData.img3 = imgString3
            }

            if (image3id.trim() == "0") {

                imageData.img4 = imgString3
            }

            if (image4id.trim().toString() == "0") {

                imageData.img5 = imgString4

            }

            if (image5id.trim().toString() == "0") {

                imageData.img6 = imgString5

            }

            allData.event_id = eventID.toString()
            allData.event_name = bind.etxtEventNameEdit.text.toString()
            allData.description = bind.etxtEventDiscriptionEdit.text.toString()
            allData.fees = bind.etxtFeesEdit.text.toString()
            allData.location = locationId.toString()
            allData.sportTypes = selectedSportList.trim()
            allData.date = bind.txtDateEdit.text.toString().trim()
            allData.noParticipants = bind.etxtNoParticipantsEdit.text.toString()
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

            if (addImageViewmodel!!.checkValidData(imageData)) {


                addImageViewmodel!!.callAddmageApi()
            }

        }

        gradeAgeRangeFunction()

        initiateApiCalls()

        bind.txtSportscoachEdit.setOnClickListener {

            bind.multispinnerEdit.performClick()
            showToast("Sports Field")
        }

    }

    private fun gradeAgeRangeFunction() {
        bind.imgGradeEdit.setOnClickListener {
            selectType(1)
        }
        bind.txtGrade1Edit.setOnClickListener {
            bind.imgGradeEdit.performClick()
        }
        bind.imgAgeEdit.setOnClickListener {
            selectType(2)
        }
        bind.txtAgeEdit.setOnClickListener {
            bind.txtAgeEdit.performClick()
        }

        val aplicableList = resources.getStringArray(R.array.ApplicableGender)

        val adapterApplicableGender: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_item,
            aplicableList
        )
        adapterApplicableGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bind.spinnerGenderEdit.adapter = adapterApplicableGender
        bind.spinnerGenderEdit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {


                    Log.e(TAG, "onItemSelected:name  " + "${aplicableList[position]}")

                    val name = aplicableList[position]

                    bind.txtSelectedApplicableGenderEdit.text = name
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
        bind.spinnerSelectMinRangeEdit.adapter = adapterMinRange
        bind.spinnerSelectMinRangeEdit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long,
                ) {
                    try {
                        if (bind.txtSelectMaxRangeEdit.text.toString() == "") {
                            bind.txtSelectMinRangeEdit.text = ageList[0]
                        } else {
                            val minRangeSelected: Int = ageList[position].toInt()
                            val maxRangeSelected: Int =
                                bind.txtSelectMaxRangeEdit.text.toString().toInt()
                            if (minRangeSelected > maxRangeSelected) {
                                Utilities.showToast(
                                    this@EditEventActivity,
                                    "Minimum age cant be more than Maximum age."
                                )
                            } else {
                                bind.txtSelectMinRangeEdit.text = ageList[position]
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        bind.txtSelectMinRangeEdit.text = ""
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    bind.txtSelectMinRangeEdit.text = ""
                }
            }
        bind.txtSelectMinRangeEdit.setOnClickListener {
            bind.spinnerSelectMinRangeEdit.performClick()
        }

        val adapterMaxRange: ArrayAdapter<String> =
            ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_item,
                ageList
            )
        adapterMaxRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bind.spinnerSelectMaxRangeEdit.adapter = adapterMaxRange
        bind.spinnerSelectMaxRangeEdit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long,
                ) {
                    try {
                        if (firstTimeMaxSelected) {
                            bind.txtSelectMaxRangeEdit.text = ageList[(ageList.size - 1)]
                            firstTimeMaxSelected = false
                        } else {
                            minRangeSelected = bind.txtSelectMinRangeEdit.text.toString().toInt()
                            maxRangeSelected = ageList[position].toInt()
                            if (maxRangeSelected < minRangeSelected) {
                                Utilities.showToast(
                                    this@EditEventActivity,
                                    "Maximum age cant be less than Minimum age."
                                )
                            } else {
                                bind.txtSelectMaxRangeEdit.text = ageList[position]
                            }
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        bind.txtSelectMaxRangeEdit.text = ""
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    bind.txtSelectMaxRangeEdit.text = ""
                }
            }
        bind.txtSelectMaxRangeEdit.setOnClickListener {
            bind.spinnerSelectMaxRangeEdit.performClick()
        }

        bind.txtSelectedGradeEdit.setOnClickListener {
            bind.spinnerSelectGradeEdit.performClick()
        }

        bind.txtlocationEdit.setOnClickListener {

            bind.spinnerLocationEdit.performClick()
        }


        bind.txtSelectedApplicableGenderEdit.setOnClickListener {

            bind.spinnerGenderEdit.performClick()
        }
//

    }


    var firstTimeMaxSelected = true
    var selectedType = 0


    private fun selectType(i: Int) {
        selectedType = i
        if (i == 1) {
            //Grade
            bind.imgGradeEdit.setImageResource(R.mipmap.rad)
            bind.imgAgeEdit.setImageResource(R.mipmap.rad1)
            bind.llGradeMainEdit.visibility = View.VISIBLE
            bind.llMinmumAgeRangeEdit.visibility = View.GONE
            bind.llMaxAgeRangeEdit.visibility = View.GONE

        } else if (i == 2) {
            //Age
            bind.imgGradeEdit.setImageResource(R.mipmap.rad1)
            bind.imgAgeEdit.setImageResource(R.mipmap.rad)
            bind.llGradeMainEdit.visibility = View.GONE
            bind.llMinmumAgeRangeEdit.visibility = View.VISIBLE
            bind.llMaxAgeRangeEdit.visibility = View.VISIBLE
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
                Log.e("sports", "onFail:$error ")

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
                    bind.img1Edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, bind.img1Edit)
                    imgString1 = pickImage!!.getImage()!!
                }

                2 -> {
                    bind.img2Edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, bind.img2Edit)
                    imgString2 = pickImage!!.getImage()!!
                }

                3 -> {
                    bind.img3Edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, bind.img3Edit)
                    imgString3 = pickImage!!.getImage()!!
                }

                4 -> {
                    bind.img4Edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, bind.img4Edit)
                    imgString4 = pickImage!!.getImage()!!
                }

                5 -> {
                    bind.img5Edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, bind.img5Edit)
                    imgString5 = pickImage!!.getImage()!!
                }

                6 -> {
                    bind.img6Edit.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, bind.img6Edit)
                    imgString6 = pickImage!!.getImage()!!
                }
            }
        }
    }

    interface DoneInterFace {
        fun done(str: String)
    }

    class AddImageObject {
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
        bind.topBar.txtTitle.text = "EDIT EVENT"
        bind.topBar.imgBack.visibility = View.VISIBLE
        bind.topBar.imgBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onLocationListSuccess(response: List<LocationResult?>?) {


        if (response != null) {
            // hideLoader()
            Log.e(TAG, "onLocationListSuccess: ====${response.toString()}")


            val adapterLoction =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, response!!)
            adapterLoction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            bind.spinnerLocationEdit.adapter = adapterLoction
            bind.spinnerLocationEdit.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        try {

                            var selectedSprefrenceItem =
                                bind.spinnerLocationEdit.selectedItem as LocationResult
                            Log.d(
                                TAG,
                                "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getName()
                            )

                            locationId = selectedSprefrenceItem.getId().toString()
                            bind.txtlocationEdit.text = selectedSprefrenceItem.getName().toString()

                        } catch (e: Exception) {
                            e.printStackTrace()

                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {

                    }
                }

        } else {
            //hideLoader()
        }
        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        // showLoader()

        coachSportsListCOntroller = CoachSportsListController(this, this)
        coachSportsListCOntroller.callCoachSportsListApi(id, token, id)

    }

    override fun onCoatListSuccess(response: List<Coat?>?) {

        //hideLoader()
    }


    override fun onGetSportsListSuccess(response: List<CoachSportsListResult?>?) {

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        if (response != null) {
            Log.e("Sports", "onGetSportsListSuccess:>>7 " + response.toString() + "")
            //hideLoader()
            val adapterSports = ArrayAdapter(this, android.R.layout.simple_spinner_item, response!!)
            adapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bind.multispinnerEdit.adapter = adapterSports
            bind.multispinnerEdit.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        try {
//                    txtSportscoach.text = response!![position]?.getSportsName().toString()
                            var selectedSprefrenceItem =
                                bind.multispinnerEdit.selectedItem as CoachSportsListResult
                            Log.d(
                                TAG,
                                "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getSportsName()
                            )
                            selectedSportList = selectedSprefrenceItem.getId().toString()
                            bind.txtSportscoachEdit.text =
                                selectedSprefrenceItem.getSportsName().toString()


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {

                        bind.txtSportscoachEdit.text = "Select Sports"
                    }
                }
        } else {
            // hideLoader()
        }
        // showLoader()

        gradeController = GetGradeListController(this, this)
        gradeController.callGetGradeListApi(id, token)


    }

    override fun onGradeListSuccess(gradeList: List<GradeListResult?>?) {
        // hideLoader()
        val adapterGrade = ArrayAdapter(this, android.R.layout.simple_spinner_item, gradeList!!)
        adapterGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        bind.spinnerSelectGradeEdit.adapter = adapterGrade
        bind.spinnerSelectGradeEdit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long,
                ) {
                    try {
                        var selectedGradeItem =
                            bind.spinnerSelectGradeEdit.selectedItem as GradeListResult
                        selectedgradeID = selectedGradeItem.getId().toString()
                        Log.d(
                            TAG,
                            "onItemSelected: " + selectedGradeItem.getId() + "  " + selectedGradeItem.getName()
                        )

                        bind.txtSelectedGradeEdit.text = selectedGradeItem.getName().toString()

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }

    }

    override fun onFail(message: String?, e: Exception?) {
        Log.e("onFail==", "message:>>7 " + message.toString() + "")

        // hideLoader()
        showToast(message)
    }

    override fun onResume() {
        super.onResume()
        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        locationController = LocationController(this, this)
        locationController.callLocationApi(id, token)
    }

}