package com.e.jannet_stable_code.ui.coachApp.coachUpdateProfileScreen

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
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.SelectedSportsGridListAdapter
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.GetProfileController
import com.e.jannet_stable_code.retrofit.controller.GetSportsListController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.response.GetProfileCoachApiResponse
import com.e.jannet_stable_code.retrofit.response.SportsListResponse
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.SpinnerPickerDialog
import com.e.jannet_stable_code.viewinterface.ILocationView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_coach_update_profile.*
import kotlinx.android.synthetic.main.topbar_layout.*
import java.util.*
import kotlin.collections.ArrayList

class CoachUpdateProfileActivity : BaseActivity() {
    private var pickImage: PickImage? = null
    private var genderFlag = 0
    private var latlong:String?=null
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_update_profile)

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
        txtlocation_update.setOnClickListener {

            spinnerLocation_update.performClick()
        }

        txtEdit.setOnClickListener {
            val coachUpdateProfileModel = CoachUpdateProfileModel(this)

            val imageStr = try {
                pickImage?.getImage()!!
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ""
            }

            if (coachUpdateProfileModel.checkValidData(
                    image = imageStr,
                    fname = etxtFName.text.toString().trim(),
                    email = etxtEmail.text.toString().trim(),
                    phNo = etxtPhNo.text.toString().trim(),
                    bdate = txtBDate.text.toString().trim(),
                    gender = genderFlag.toString().trim(),
                    location = "" ,
                    sportsIds = sportsIds
                )
            ) {
                //latlong
                coachUpdateProfileModel.updateCoachDetails()
            }
        }
        imgMale.setOnClickListener {
            genderFlag = 1
            imgMale.setImageResource(R.mipmap.rad)
            imgFemale.setImageResource(R.mipmap.rad1)
        }
        txtMale.setOnClickListener { imgMale.performClick() }
        imgFemale.setOnClickListener {
            genderFlag = 2
            imgMale.setImageResource(R.mipmap.rad1)
            imgFemale.setImageResource(R.mipmap.rad)
        }
        txtFemale.setOnClickListener { imgFemale.performClick() }
        txtBDate.setOnClickListener {
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
                    txtBDate.text="$day-"+(month+1)+"-$year"
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

        etxtSports.setOnClickListener {
            multispinner.setDataList(sportsStrList)
            multispinner.performClick()
        }

        multispinner.setMultiSpinnerListener {
            if (firstTimeSports) {
                firstTimeSports = false
            }

            var string = ""
            val list = it
            etxtSports.text = ""
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
            etxtSports.text = string
        }

        imgProfile.setOnClickListener {
            pickImage = PickImage(this@CoachUpdateProfileActivity)
        }

        selectedSportsGridListAdapter = SelectedSportsGridListAdapter(this)
        rcvSelectedSportsList.adapter = selectedSportsGridListAdapter
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
        if (pickImage != null) pickImage!!.activityResult(requestCode, resultCode, data, imgProfile)


        if (requestCode==100 && resultCode== RESULT_OK){

            // intlize place
            val place = Autocomplete.getPlaceFromIntent(data)

            // set address on our edit text
            txtlocation_update.setText(place.address)

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
                .into(imgProfile)

            Log.d(TAG, "setData: testNAME>>" + data.name)
            etxtFName.setText(data.name)
            etxtEmail.text = data.email
            etxtPhNo.setText(data.contactNo)
            txtBDate.text = data.birthdate
            txtlocation_update.text=(data.location)
//            spinnerLocation_update.setEmptyTitle(data.location.toString())
            if (data.gender == "m") imgMale.performClick()
            if (data.gender == "f") imgFemale.performClick()
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
            etxtSports.text = str


            selectedSportsGridListAdapter!!.setListData(imageStrList)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

            Log.d(TAG, "setData: testNAME>>" + e.printStackTrace())
        }
    }

    private val TAG = "CoachUpdateProfileActiv"

    private fun setTopBar() {
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.coach_profile)
        txtTitle.textSize = 22F
    }
}