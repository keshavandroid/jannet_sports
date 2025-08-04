package com.xtrane.ui.coachApp.addEventScreen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.xtrane.R
import com.xtrane.adapter.NothingSelectedSpinnerAdapter
import com.xtrane.databinding.ActivityAddEventBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.coachsportslistdata.CoachSportsListResult
import com.xtrane.retrofit.controller.CoachSportsListController
import com.xtrane.retrofit.controller.GetGradeListController
import com.xtrane.retrofit.controller.GetSportsListController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachSportsListController
import com.xtrane.retrofit.controller.IGetGradeListController
import com.xtrane.retrofit.controller.IGetSportController
import com.xtrane.retrofit.controller.ILocationController
import com.xtrane.retrofit.controller.IMinMaxAgeController
import com.xtrane.retrofit.controller.LocationController
import com.xtrane.retrofit.controller.MinMaxAgeController
import com.xtrane.retrofit.gradlistdata.GradeListResult
import com.xtrane.retrofit.locationdata.Coat
import com.xtrane.retrofit.locationdata.LocationResult
import com.xtrane.retrofit.minmaxagedata.MinMaxAgeResult
import com.xtrane.retrofit.response.GetChildGroupListResponse
import com.xtrane.retrofit.response.SportsListResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.coachApp.AddNewLocationActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.DatePickerResult
import com.xtrane.utils.PickImage
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.TAG
import com.xtrane.utils.datePicker
import com.xtrane.viewinterface.ICoachSportsListVIew
import java.text.SimpleDateFormat
import com.xtrane.viewinterface.IGetGradeListView
import com.xtrane.viewinterface.ILocationView
import com.xtrane.viewinterface.IMinMAxAgeView
import java.text.ParseException
import java.util.Date
import java.util.Calendar


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
    private lateinit var binding: ActivityAddEventBinding
    var spnEventType = ""

    override fun getController(): IBaseController? {

        return null
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_add_event)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // location spnner

        binding.imgGrade.setImageResource(R.mipmap.rad)

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)


        minMaxAgeController = MinMaxAgeController(this, this)

        locationController = LocationController(this, this)
        locationController.callLocationApi(id, token)

        Log.e(TAG, "=====$id=======$token")

        binding.txtlocation.text = "Select Location"


//        coachSportsListCOntroller = CoachSportsListController(this,this)
//        coachSportsListCOntroller.callCoachSportsListApi(id,token,id)
////
        showLoader()

//        getSportController = GetSportsController(this, this)
//        getSportController.callGetSportsApi(id, token)

        binding.btnAddLocation.setOnClickListener {

            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Add Location")
                .setMessage("You Wan to Add New Location?")
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        var intent = Intent(this, AddNewLocationActivity::class.java)
                        startActivity(intent)
                    })
                .setNegativeButton("No", null)
                .show()

        }
        // Initialize Places
        Places.initialize(applicationContext, "AIzaSyCl7SGfKSyWLEqVkpiUtur0tZ_yqJzED7I")

        // Set up autocomplete for address field

        val placesClient = Places.createClient(this)
        val sessionToken = AutocompleteSessionToken.newInstance()





        binding.etxtAddress.addTextChangedListener { text ->

            val query = text.toString()

            if (query.isNotEmpty()) {

                val request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(query)
                    .setSessionToken(sessionToken)
                    .build()

                placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener { response ->
                        val suggestions = response.autocompletePredictions.map {
                            it.getFullText(null).toString()
                        }

                        Log.e("suggestion==", suggestions.toString())

//                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, suggestions)
//                        listViewSuggestions.adapter = adapter

                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }


        }


        addEventObject = AddEventObject()
        addEventViewModel = AddEventViewModel(this)

        setTopBar()
        SetEventTypeSpinner()


        val adapterEventDuration = ArrayAdapter(this@AddEventActivity, android.R.layout.simple_spinner_item,
           resources.getStringArray(R.array.duration_types)
        )
        adapterEventDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDurationType.adapter = adapterEventDuration

        binding.spnDurationType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDurationType = parent?.getItemAtPosition(position).toString()
                // Handle the selected duration type
                Log.d(TAG, "Selected duration type: $selectedDurationType")

                if (selectedDurationType.equals("Daily", ignoreCase = true)) {
                    val durationValues = (1..30).map { it.toString()+" days" }.toTypedArray()
                    val adapterDurationValue = ArrayAdapter(
                        this@AddEventActivity,
                        android.R.layout.simple_spinner_item,
                        durationValues
                    )
                    adapterDurationValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnDurationTypevalue.adapter = adapterDurationValue
                }
                else  if (selectedDurationType.equals("Weekly", ignoreCase = true)) {
                    val durationValues = (1..4).map { it.toString() +" Week"}.toTypedArray()
                    val adapterDurationValue = ArrayAdapter(
                        this@AddEventActivity,
                        android.R.layout.simple_spinner_item,
                        durationValues
                    )
                    adapterDurationValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnDurationTypevalue.adapter = adapterDurationValue
                }
                else
                {
                    val durationValues = (1..12).map { it.toString() +" Months  "}.toTypedArray()
                    val adapterDurationValue = ArrayAdapter(
                        this@AddEventActivity,
                        android.R.layout.simple_spinner_item,
                        durationValues
                    )
                    adapterDurationValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spnDurationTypevalue.adapter = adapterDurationValue
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected if needed
            }
        }





        binding.img1.setOnClickListener {
            imagePickFlag = 1
            pickImage = PickImage(this@AddEventActivity)
        }

        binding.deleteIconImg1.setOnClickListener {
            imgString1=""
            binding.img1.setImageResource(R.mipmap.plus) // Reset to default or hide
        }
        binding.img2.setOnClickListener {
            imagePickFlag = 2
            pickImage = PickImage(this@AddEventActivity)
        }
        binding.deleteIconImg2.setOnClickListener {
            imgString2=""
            binding.img2.setImageResource(R.mipmap.plus) // Reset to default or hide
        }
        binding.img2.setOnClickListener {
            imagePickFlag = 2
            pickImage = PickImage(this@AddEventActivity)
        }
        binding.deleteIconImg3.setOnClickListener {
            imgString3=""
            binding.img3.setImageResource(R.mipmap.plus) // Reset to default or hide
        }
        binding.img3.setOnClickListener {
            imagePickFlag = 3
            pickImage = PickImage(this@AddEventActivity)
        }
        binding.deleteIconImg4.setOnClickListener {
            imgString4=""
            binding.img4.setImageResource(R.mipmap.plus) // Reset to default or hide
        }
        binding.img4.setOnClickListener {
            imagePickFlag = 4
            pickImage = PickImage(this@AddEventActivity)
        }
        binding.deleteIconImg5.setOnClickListener {
            imgString5=""
            binding.img5.setImageResource(R.mipmap.plus) // Reset to default or hide
        }
        binding.img5.setOnClickListener {
            imagePickFlag = 5
            pickImage = PickImage(this@AddEventActivity)
        }
        binding.deleteIconImg6.setOnClickListener {
            imgString6=""
            binding.img6.setImageResource(R.mipmap.plus) // Reset to default or hide
        }
        binding.img6.setOnClickListener {
            imagePickFlag = 6
            pickImage = PickImage(this@AddEventActivity)
        }
        binding.txtDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setTheme(R.style.ThemeOverlay_App_DatePicker)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                // Respond to positive button click.
                val selectedDate = datePicker.selection
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                if (selectedDate != null) {
                  //  binding.txtDate.text = convertDateFormat(sdf.format(selectedDate))
                    binding.txtDate.text = sdf.format(selectedDate)
                }
            }
            datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        }

        binding.txtTime1.setOnClickListener {

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    // Format the time and set it to the TextView
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    binding.txtTime1.text = formattedTime
                },
                hour,
                minute,
                true // Use 24-hour format
            )
            timePickerDialog.show()        }


        binding.txtAdd.setOnClickListener {

            val allData = AddEventObject()
            allData.img1 = imgString1
            allData.img2 = imgString2
            allData.img3 = imgString3
            allData.img4 = imgString4
            allData.img5 = imgString5
            allData.img6 = imgString6
            allData.event_name = binding.etxtEventName.text.toString().trim()
            allData.description = binding.etxtEventDiscription.text.toString()

            allData.fees = binding.etxtFees.text.toString().trim()
            allData.location = locationId.toString()
            allData.sportTypes = selectedSportList.trim()
            allData.eventType = spnEventType
            allData.coach_id = id.toString()

            allData.date = binding.txtDate.text.toString().trim()
            allData.time = binding.txtTime1.text.toString().trim()

            allData.noParticipants = binding.etxtNoParticipants.text.toString().trim()
            allData.gender_applicable = binding.txtSelectedApplicableGender.text.toString()
            allData.grade_id = selectedgradeID.toString().trim()
            Log.e("TAG", "onCreate: selected grade id is ==$selectedgradeID")
            allData.minimum_age = minRangeSelected.toString()
            allData.max_age = maxRangeSelected.toString()
            allData.min_grade = selectedgradeID.toString()
            allData.max_grade = selectedMaxgradeID.toString()
            Log.e("TAG", "onCreate: minimum_age ==" + allData.minimum_age)
            Log.e("TAG", "onCreate: max_age ==" + allData.max_age)

            if (addEventViewModel!!.checkValidData(allData)) {

                if (binding.etxtEventName.text == null) {
                    Toast.makeText(this, "Please Enter Event Name", Toast.LENGTH_LONG).show()
                } else if (binding.etxtEventDiscription.text == null) {
                    Toast.makeText(this, "Please Enter Event Description", Toast.LENGTH_LONG).show()
                } else if (binding.txtSelectedApplicableGender.text.trim()
                        .toString() == "Select Applicable Gender"
                ) {
                    showToast("Selcet Applicable Gender")
                } else if (binding.txtlocation.text.trim().toString() == "Select Location") {
                    showToast("Select Location")
                } else if (binding.txtSportscoach.text.trim().toString() == "Select Sports") {
                    showToast("Select Sports")
                } else if (spnEventType == "") {
                    showToast("Select Event Type")
                } else {

                    Log.e("TAG", "onCreate:selectedType1==$selectedType")

                    if (selectedType == 1) {

                        Log.e("TAG", "onCreate:selectedType2==$selectedType")

                        if (binding.txtSelectedGrade.text.trim()
                                .toString() == "Select Minimum Grade"
                        ) {
                            showToast("Select Starting Grade")
                        } else if (binding.txtSelectedGradeMax.text.trim()
                                .toString() == "Select Maximum Grade"
                        ) {
                            showToast("Select Maximum Grade")
                        } else {

                            Log.e("TAG", "onCreate:selectedType3==$selectedType")

                            addEventViewModel!!.callAddEventApi()

                        }
                    } else if (selectedType == 2) {
                        if (binding.txtSelectMinRange.text.trim()
                                .toString() == "Select Minimum Age"
                        ) {
                            showToast("Select Minimum Age")
                        } else if (binding.txtSelectMaxRange.text.trim()
                                .toString() == "Select Maximum Age"
                        ) {
                            showToast("Select Maximum Age")
                        } else {

                            addEventViewModel!!.callAddEventApi()

                        }
                    }
                }
            }

        }

        gradeAgeRangeFunction()

        initiateApiCalls()

        binding.txtSportscoach.setOnClickListener {

            binding.multispinner.performClick()
        }

        binding.txtEventType.setOnClickListener {

            binding.multispinnerforEventType.performClick()
        }
        binding.txtlocation.setOnClickListener {

            binding.spinnerLocation.performClick()

        }

        binding.txtSelectedApplicableGender.setOnClickListener {


            binding.spinnerGender.performClick()

        }


        binding.txtSelectedGrade.setOnClickListener {
            binding.spinnerSelectGrade.performClick()
        }
        binding.txtSelectedGradeMax.setOnClickListener {

            binding.spinnerSelectGradeMax.performClick()
            if (binding.txtSelectedGrade.text.trim().toString() == "Select Starting Grade") {

                showToast("Select Minimum Grade First")
            } else {

            }
        }

        binding.txtSelectMaxRange.setOnClickListener {
            binding.spinnerSelectMaxRange.performClick()
        }

        binding.txtSelectMinRange.setOnClickListener {
            binding.spinnerSelectMinRange.performClick()
        }

    }

    private fun SetEventTypeSpinner() {

        var eventtype = ArrayList<String?>()
        eventtype.add("Single Match")
        eventtype.add("Tournament")
        eventtype.add("Practice")


        val adapterSports = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventtype!!)
        adapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.multispinnerforEventType.adapter = adapterSports


        binding.multispinnerforEventType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {

                    Log.e("Spinner=", "onItemSelected called")
                    spnEventType = p0!!.getItemAtPosition(pos).toString()
                    Log.e("Spinner=", spnEventType + "==")
                    binding.txtEventType.text = spnEventType
                    Log.e(TAG, "onItemSelected: ")

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.e("Spinner=", "onNothingSelected=" + p0 + "==")
                }

            }
    }

    private fun AgerangeFunction() {

        binding.txtSelectMinRange.text = "Select Minimum Age"
        binding.txtSelectMaxRange.text = "Select Maximum Age"

        Log.e("txtSelectMinRange.isvisible=", binding.txtSelectMinRange.isVisible.toString())
        Log.e("txtSelectMaxRange.isvisible=", binding.txtSelectMaxRange.isVisible.toString())
        val tempminAgeListNew = ArrayList<String?>()

        if (minMaxgeResult!!.isNotEmpty()) {
            val tempminAgeList = ArrayList<String?>()

            for (i in minMaxgeResult!![0]!!.getMinAgeStart()!!
                .toInt()..minMaxgeResult!![0]!!.getMinAgeEnd()!!
                .toInt()) {

                tempminAgeList.add("$i")

            }
            tempminAgeListNew.add(0, "Select Minimum Age")
            tempminAgeListNew.addAll(1, tempminAgeList)

            Log.e(TAG, "onMinMaxAgeSuccess: minmimum age list $tempminAgeList")

            val adapterMinRange: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    tempminAgeListNew
                )
            adapterMinRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSelectMinRange.adapter = adapterMinRange
            binding.spinnerSelectMinRange.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        try {
                            binding.txtSelectMinRange.text = tempminAgeListNew[position]

                            val minimumselectedAge = tempminAgeListNew[position]

                            Log.e("Select Minimum Age==", minimumselectedAge.toString())
//                            if (minimumselectedAge != "Select Minimum Age") {
//                                binding.txtSelectMinRange.text = minimumselectedAge
//                                minRangeSelected = minimumselectedAge!!.toInt()
//                            }
//                            else
//                            {
//                                binding.txtSelectMinRange.text = "Select Minimum Age"
//
//                            }

                            if (position == 0) {
                                // Show default text
                                binding.txtSelectMinRange.text = "Select Minimum Age"
                            } else {
                                val selected = tempminAgeList[position]
                                binding.txtSelectMinRange.text = selected
                                minRangeSelected = selected!!.toInt()
                                // Build max age list here

                                temparraylist = ArrayList<String>()
                                temparraylist.add(0, "Select Maximum Age")

                                for (i in minimumselectedAge?.toInt()
                                    ?.plus(1)!!..minMaxgeResult!![0]!!.getMaxAgeEnd()
                                    ?.toInt()!!) {


                                    temparraylist.add("$i")
                                    tempmaxAgeList.add("$i")

                                    Log.e(
                                        TAG,
                                        "onMinMaxAgeSuccess: after mimimum age temp array listselection $temparraylist  "
                                    )

                                    Log.e(
                                        TAG,
                                        "onMinMaxAgeSuccess:tempmap age list  minimiiiii $tempmaxAgeList"
                                    )

                                }
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
                            binding.spinnerSelectMaxRange.adapter = adapterMaxRange
                            binding.spinnerSelectMaxRange.onItemSelectedListener =
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
                                            if (position == 0) {
                                                // Show default text
                                                binding.txtSelectMaxRange.text = "Select Maximum Age"
                                            }
                                            else
                                            {

                                                binding.txtSelectMaxRange.text = temparraylist[position]
                                                maxRangeSelected = temparraylist[position].toInt()

                                            }

                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            binding.txtSelectMaxRange.text = ""
                                        }
                                    }

                                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                                        binding.txtSelectMaxRange.text = ""
                                    }
                                }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            binding.txtSelectMinRange.text = ""
                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {
                        binding.txtSelectMinRange.text = ""
                    }
                }


            //select max range

//            var minmum_age = binding.txtSelectMinRange.text.toString()
//
//
//            for (i in minMaxgeResult!![0]!!.getMaxAgeStart()!!
//                .toInt()..minMaxgeResult!![0]!!.getMaxAgeEnd()!!
//                .toInt()) {
//
////                tempmaxAgeList.add("$i")
////            if (minmum_age.trim() == "Select Minimum Age") {
////
////                minmum_age = "0"
////                if (minmum_age.toInt() < i) {
////
////                    tempmaxAgeList.add("$i")
////                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age ceck if $tempmaxAgeList  ")
////
////                } else {
////                    tempmaxAgeList.add("$i")
////                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age  not check if   $tempmaxAgeList")
////
////
////                }
////            } else {
////
////
////                if (minmum_age.toInt() < i) {
////
////                    tempmaxAgeList.add("$i")
////                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age ceck else $tempmaxAgeList  ")
////
////                } else {
////                    tempmaxAgeList.add("$i")
////                    Log.e(TAG, "onMinMaxAgeSuccess:mimimum age  not check else  $tempmaxAgeList")
////
////
////                }
////            }
//
//            }
//
//            Log.e(TAG, "onMinMaxAgeSuccess: maximum age list $tempmaxAgeList")

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
        binding.imgGrade.setOnClickListener {
            selectType(1)
        }
        binding.txtGrade1.setOnClickListener {
            binding.imgGrade.performClick()
        }
        binding.imgAge.setOnClickListener {
            selectType(2)
        }
        binding.txtAge.setOnClickListener {
            binding.imgAge.performClick()
        }

        val aplicableList = resources.getStringArray(R.array.ApplicableGender)

        val adapterApplicableGender: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_item,
            aplicableList
        )
        adapterApplicableGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapterApplicableGender
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                binding.txtSelectedApplicableGender.text = name.toString()
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
    var selectedType = 1
    private fun selectType(i: Int) {

        selectedType = i

        if (i == 1) {
            //Grade
            binding.imgGrade.setImageResource(R.mipmap.rad)
            binding.imgAge.setImageResource(R.mipmap.rad1)
            binding.llGradeMain.visibility = View.VISIBLE
            binding.llGradeMainMax.visibility = View.VISIBLE
            binding.llMinmumAgeRange.visibility = View.GONE
            binding.llMaxAgeRange.visibility = View.GONE
            minRangeSelected = 0
            maxRangeSelected = 0
        } else if (i == 2) {
            //Age
            binding.imgGrade.setImageResource(R.mipmap.rad1)
            binding.imgAge.setImageResource(R.mipmap.rad)
            binding.llGradeMain.visibility = View.GONE
            binding.llGradeMainMax.visibility = View.GONE
            binding.llMinmumAgeRange.visibility = View.VISIBLE
            binding.llMaxAgeRange.visibility = View.VISIBLE
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
                    binding.img1.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, binding.img1)
                    imgString1 = pickImage!!.getImage()!!
                }

                2 -> {
                    binding.img2.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, binding.img2)
                    imgString2 = pickImage!!.getImage()!!
                }

                3 -> {
                    binding.img3.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, binding.img3)
                    imgString3 = pickImage!!.getImage()!!
                }

                4 -> {
                    binding.img4.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, binding.img4)
                    imgString4 = pickImage!!.getImage()!!
                }

                5 -> {
                    binding.img5.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, binding.img5)
                    imgString5 = pickImage!!.getImage()!!
                }

                6 -> {
                    binding.img6.setPadding(0)
                    pickImage!!.activityResult(requestCode, resultCode, data, binding.img6)
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
        binding.spinner.prompt = "Select child group"

        binding.spinner.adapter = NothingSelectedSpinnerAdapter(
            adapter,
            R.layout.contact_spinner_row_nothing_selected,  // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
            this
        )
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                try {
                    binding.txtChildGroup.text = childAgeList!![position - 1]!!.name
                    selectedChildAgeGroupId = childAgeList!![position - 1]!!.id.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.txtChildGroup.text = ""
                    selectedChildAgeGroupId = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                binding.txtChildGroup.text = ""
                selectedChildAgeGroupId = ""
            }
        }
        binding.txtChildGroup.setOnClickListener { binding.spinner.performClick() }
    }

    private fun setTopBar() {
        binding.topbar.txtTitle.text = resources.getString(R.string.add_event)
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener {
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

            binding.spinnerLocation.adapter = adapterLoction

            binding.spinnerLocation.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long,
                    ) {
                        try {

                            var selectedSprefrenceItem =
                                binding.spinnerLocation.selectedItem as LocationResult
                            Log.d(
                                TAG,
                                "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getName()
                            )

                            locationId = selectedSprefrenceItem.getId().toString()
                            binding.txtlocation.text = selectedSprefrenceItem.getName().toString()

                        } catch (e: Exception) {
                            e.printStackTrace()
                            binding.txtlocation.text = ""

                        }
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>?) {

                        binding.txtlocation.text = "Select Location"
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

        binding.multispinner.adapter = adapterSports
        binding.multispinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long,
            ) {
                try {

                    var selectedSprefrenceItem =
                        binding.multispinner.selectedItem as CoachSportsListResult
                    Log.d(
                        TAG,
                        "onItemSelected: " + selectedSprefrenceItem.getId() + "  " + selectedSprefrenceItem.getSportsName()
                    )

                    selectedSportList = selectedSprefrenceItem.getId().toString()
                    binding.txtSportscoach.text = selectedSprefrenceItem.getSportsName().toString()
                    Log.e(TAG, "onItemSelected: ")


                } catch (e: Exception) {
                    e.printStackTrace()

                    binding.txtSportscoach.text = ""
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                binding.txtSportscoach.text = "Select Sports"
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
        lisBtHint.setName("Select Starting Grade")
        tempGradeTypeList.add(0, lisBtHint)
        tempGradeTypeList.addAll(gradeListResult!!)

        hideLoader()
        val adapterGrade =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, tempGradeTypeList!!)
        adapterGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        tempGradeMaxList = ArrayList<GradeListResult?>()

        binding.spinnerSelectGrade.adapter = adapterGrade
        binding.spinnerSelectGrade.onItemSelectedListener =
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
                            binding.spinnerSelectGrade.selectedItem as GradeListResult
                        selectedgradeID = selectedGradeItem.getId().toString()
                        Log.d(
                            TAG,
                            "onItemSelected: " + selectedGradeItem.getId() + "  " + selectedGradeItem.getName()
                        )
                        binding.txtSelectedGrade.text = selectedGradeItem.getName().toString()


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

                        binding.spinnerSelectGradeMax.adapter = adapteMaxrGrade
                        binding.spinnerSelectGradeMax.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parentView: AdapterView<*>?,
                                    selectedItemView: View?,
                                    position: Int,
                                    id: Long,
                                ) {

                                    try {

                                        var selectedGradeItem =
                                            binding.spinnerSelectGradeMax.selectedItem as GradeListResult
                                        selectedMaxgradeID = selectedGradeItem.getId().toString()
                                        Log.d(
                                            TAG,
                                            "onItemSelected: max grade " + selectedGradeItem.getId() + "  " + selectedGradeItem.getName()
                                        )
                                        binding.txtSelectedGradeMax.text =
                                            selectedGradeItem.getName().toString()

                                        Log.d(
                                            TAG,
                                            "onItemSelectedd  after select max from drop down ${binding.txtSelectedGradeMax.text}"
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
        var eventType = ""
        var date = ""
        var time = ""
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

    override fun onResume() {
        super.onResume()

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        locationController = LocationController(this, this)
        locationController.callLocationApi(id, token)

    }
    fun convertDateFormat(strDate: String): String {
        try {
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "dd MMM yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val outputFormat = SimpleDateFormat(outputPattern)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormat.parse(strDate)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str!!

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

}