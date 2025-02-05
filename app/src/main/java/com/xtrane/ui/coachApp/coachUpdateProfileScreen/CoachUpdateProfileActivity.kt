package com.xtrane.ui.coachApp.coachUpdateProfileScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.xtrane.R
import com.xtrane.adapter.SelectedSportsGridListAdapter
import com.xtrane.databinding.ActivityCoachUpdateProfileBinding
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.GetProfileController
import com.xtrane.retrofit.controller.GetSportsListController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.response.GetProfileCoachApiResponse
import com.xtrane.retrofit.response.SportsListResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.PickImage
import com.xtrane.utils.SpinnerPickerDialog
import com.xtrane.viewinterface.ILocationView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*
import kotlin.collections.ArrayList

class CoachUpdateProfileActivity : BaseActivity() {
    private var pickImage: PickImage? = null
    private var genderFlag = 0
    private var latlong:String?=null
    override fun getController(): IBaseController? {
        return null
    }
    private lateinit var bind: ActivityCoachUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_coach_update_profile)

        bind = ActivityCoachUpdateProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)

        setTopBar()

        // for auto complete locatiion
        Places.initialize(applicationContext,"AIzaSyCl7SGfKSyWLEqVkpiUtur0tZ_yqJzED7I")
        /*etxtLocation.isFocusable = false
         etxtLocation.setOnClickListener {

            val fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME)

            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(this)

            startActivityForResult(intent,100)
        }*/
        bind.txtlocationUpdate.setOnClickListener {

            bind.spinnerLocationUpdate.performClick()
        }

        bind.txtEdit.setOnClickListener {
            val coachUpdateProfileModel = CoachUpdateProfileModel(this)

            val imageStr = try {
                pickImage?.getImage()!!
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ""
            }

            if (coachUpdateProfileModel.checkValidData(
                    image = imageStr,
                    fname = bind.etxtFName.text.toString().trim(),
                    email = bind.etxtEmail.text.toString().trim(),
                    phNo = bind.etxtPhNo.text.toString().trim(),
                    bdate = bind.txtBDate.text.toString().trim(),
                    gender = genderFlag.toString().trim(),
                    location = "" ,
                    sportsIds = sportsIds
                )
            ) {
                //latlong
                coachUpdateProfileModel.updateCoachDetails()
            }
        }
        bind.imgMale.setOnClickListener {
            genderFlag = 1
            bind.imgMale.setImageResource(R.mipmap.rad)
            bind.imgFemale.setImageResource(R.mipmap.rad1)
        }
        bind.txtMale.setOnClickListener { bind.imgMale.performClick() }
        bind.imgFemale.setOnClickListener {
            genderFlag = 2
            bind.imgMale.setImageResource(R.mipmap.rad1)
            bind.imgFemale.setImageResource(R.mipmap.rad)
        }
        bind.txtFemale.setOnClickListener { bind.imgFemale.performClick() }
        bind.txtBDate.setOnClickListener {
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
                    bind.txtBDate.text="$day-"+(month+1)+"-$year"
                }

                override fun onCancel() {}
                override fun onDismiss() {}
            })
            spinnerPickerDialog.show(this.supportFragmentManager, "")
        }

        GetProfileController(this, false, object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as GetProfileCoachApiResponse
                    setData(data.getResult()?.get(0))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                getSportsList()
            }
        })

        bind.etxtSports.setOnClickListener {
            bind.multispinner.setDataList(sportsStrList)
            bind.multispinner.performClick()
        }

        bind.multispinner.setMultiSpinnerListener {
            if (firstTimeSports) {
                firstTimeSports = false
            }

            var string = ""
            val list = it
            bind.etxtSports.text = ""
            sportsIds = ""
            val listImageStr = ArrayList<String>()
            for (i in list.indices) {
                try {

                    if (list[i]) {
                        if (string == "") {
                            string = sportsList[i]!!.sportsName!!
                            sportsIds = sportsList[i]!!.id.toString()
                        } else {
                            string += ", " + sportsList[i]!!.sportsName!!
                            sportsIds += ", " + sportsList[i]!!.id.toString()
                        }
                        listImageStr.add(sportsList[i]!!.image!!)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            selectedSportsGridListAdapter!!.setListData(listImageStr)
            bind.etxtSports.text = string
        }

        bind.imgProfile.setOnClickListener {
            pickImage = PickImage(this@CoachUpdateProfileActivity)
        }

        selectedSportsGridListAdapter = SelectedSportsGridListAdapter(this)
        bind.rcvSelectedSportsList.adapter = selectedSportsGridListAdapter
    }

    private var selectedSportsGridListAdapter: SelectedSportsGridListAdapter? = null
    private var firstTimeSports = true
    private var sportsIds = ""
    var sportsList = ArrayList<SportsListResponse.Result?>()
    var sportsStrList = ArrayList<String>()
    private fun getSportsList() {
        GetSportsListController(this, object : ControllerInterface {
            override fun onFail(error: String?) {

            }

            override fun <T> onSuccess(response: T, method: String) {
                try {
                    val data = response as SportsListResponse
                    sportsList = ArrayList(data.getResult()!!)

                    for (i in sportsList.indices) {
                        sportsStrList.add(sportsList[i]!!.sportsName!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }).callApi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (pickImage != null) pickImage!!.activityResult(requestCode, resultCode, data, bind.imgProfile)


        if (requestCode==100 && resultCode== RESULT_OK){

            // intlize place
            val place = Autocomplete.getPlaceFromIntent(data)

            // set address on our edit text
            bind.txtlocationUpdate.setText(place.address)

            Log.e("Lating","=======Address is ====${place.address}")

            // get lat and lon

            latlong = place.latLng.toString()

            Log.e("Lating","=======lating is ====$latlong")
        }else if (resultCode== AutocompleteActivity.RESULT_ERROR){


            val status = Autocomplete.getStatusFromIntent(data)

            Log.e("Error","===eror is =$status")
            Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()


        }
    }


    private fun setData(data: GetProfileCoachApiResponse.Result?) {
        try {
            Glide.with(this)
                .load(data!!.image)
                .apply( RequestOptions().override(600, 200))
                .placeholder(R.drawable.loader_background)
                .into(bind.imgProfile)

            Log.d(TAG, "setData: testNAME>>" + data.name)
            bind. etxtFName.setText(data.name)
            bind.etxtEmail.text = data.email
            bind.etxtPhNo.setText(data.contactNo)
            bind.txtBDate.text = data.birthdate
            bind.txtlocationUpdate.text=(data.location)
//            spinnerLocation_update.setEmptyTitle(data.location.toString())
            if (data.gender == "m") bind.imgMale.performClick()
            if (data.gender == "f") bind.imgFemale.performClick()
            //etxtLocation.setText(data!!.a)

            var str = ""
            var tempSportsIds = ""
            val imageStrList = ArrayList<String>()
            for (i in data.sports!!.indices) {
                str = if (str == "") {
                    tempSportsIds= data.sports!![i].id.toString()
                    data.sports!![i].sportsName!!
                }
                else {
                    tempSportsIds += ", "+data.sports!![i].id.toString()
                    str + ", " + data.sports!![i].sportsName!!
                }
                imageStrList.add(data.sports!![i].image!!)


            }
            sportsIds=tempSportsIds
            Log.d(TAG, "setData: test>>$str")
            bind.etxtSports.text = str


            selectedSportsGridListAdapter!!.setListData(imageStrList)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            Log.d(TAG, "setData: testNAME>>" + e.printStackTrace())
        }
    }

    private val TAG = "CoachUpdateProfileActiv"

    private fun setTopBar() {
        bind.topbar.imgBack.setOnClickListener { finish() }
        bind.topbar.txtTitle.text = getString(R.string.coach_profile)
        bind.topbar.txtTitle.textSize = 22F
    }
}