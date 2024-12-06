package com.e.jannet_stable_code.ui.loginRegister.registerCoachScreen

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.ui.coachApp.SeacrchLocationGoogleActivity
import com.e.jannet_stable_code.ui.loginRegister.loginScreen.LoginActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.DatePickerResult
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.datePicker
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

import kotlinx.android.synthetic.main.activity_coach_register.*
import kotlinx.android.synthetic.main.topbar_layout.*

class RegisterCoachActivity : AppCompatActivity() {
    var pickImage: PickImage? = null
    var genderFlag = 0
    var model: RegisterCoachModel? = null
    var API_KEY ="AIzaSyDZz4wntacjlvQ3aEGb9eWpDPU6M71t_Vc"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_register)

        model = RegisterCoachModel(this)

        setTopBar()
        clicks()
      /*  val intent = Intent(this, SearchPlaceActivity::class.java)
        intent.putExtra(
            SearchPlacesStatusCodes.CONFIG,
            SearchPlaceActivity.Config.Builder(apiKey = API_KEY)
                .setSearchBarTitle("Enter Source Location")
                .setMyLocation("12.9716,77.5946")
                .setEnclosingRadius("500")
                .build()
        )*/

        tvLocation.setOnClickListener(View.OnClickListener {
           /* intent = Intent(this, SeacrchLocationGoogleActivity::class.java)
            startActivity(intent)*/
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val pair = Pair.create(
                    (tvLocation as? View),
                    SearchPlacesStatusCodes.PLACEHOLDER_TRANSITION
                )
                val options = ActivityOptions.makeSceneTransitionAnimation(this, pair).toBundle()
                startActivityForResult(intent, 700, options)

            } else {
                startActivityForResult(intent, 700)
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }*/
        })

    }

    private fun clicks() {
        imgMale.setOnClickListener {
            genderFlag = 1
            imgMale.setImageResource(R.mipmap.rad)
            imgFemale.setImageResource(R.mipmap.rad1)
        }
        imgFemale.setOnClickListener {
            genderFlag = 2
            imgFemale.setImageResource(R.mipmap.rad)
            imgMale.setImageResource(R.mipmap.rad1)
        }
        txtMale.setOnClickListener { imgMale.performClick() }
        txtFemale.setOnClickListener { imgFemale.performClick() }
        txtLogin.setOnClickListener {
            startActivity(
                Intent(this@RegisterCoachActivity, LoginActivity::class.java).putExtra(
                    Constants.USER_TYPE, Constants.COACH
                )
            )
            finish()
        }
        txtRegister.setOnClickListener {
            val data = CoachRegisterObject()

            if(pickImage!=null && pickImage!!.getImage()!=null){
                data.image = pickImage?.getImage()
            }

            data.name = etxtName.text.toString().trim()
            data.gender = genderFlag
            data.birthdate = txtBDate.text.toString().trim()
            data.email = etxtEmail.text.toString().trim()
            data.password = etxtPwd.text.toString().trim()
            data.contactNo = etxtPhNo.text.toString().trim()+ etxtPhNo1.text.toString().trim()+ etxtPhNo2.text.toString().trim()
            data.location = "*"
            //etxtLocation.text.toString().trim()

            val validData = model?.registerValidation(data)

            if (validData != null && validData) {
                model?.registerCoach()
            }

        }
        imgProfile.setOnClickListener {
            pickImage = PickImage(this@RegisterCoachActivity)
        }
        txtBDate.setOnClickListener {
            datePicker(
                txtBDate, this, supportFragmentManager, object : DatePickerResult {
                    override fun onSuccess(string: String) {

                    }
                }, false, futureDatesOnly = false
            )
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       /* if (requestCode == 700 && resultCode == Activity.RESULT_OK) {

            val placeDetails =
                data?.getParcelableExtra<PlaceDetails>(SearchPlacesStatusCodes.PLACE_DATA)

            tvLocation.setText(placeDetails?.name)

            tvLocation.text = placeDetails.toString()
            Log.i(javaClass.simpleName, "onActivityResult: ${placeDetails}  ")
        }*/
        if (pickImage != null) pickImage!!.activityResult(requestCode, resultCode, data, imgProfile)
    }
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (pickImage != null) pickImage!!.activityResult(requestCode, resultCode, data, imgProfile)
    }*/

    private fun setTopBar() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        imgLogo.visibility = View.GONE
        txtTitle.visibility = View.VISIBLE
        txtTitle.text = resources.getString(R.string.coach_register)
    }

    class CoachRegisterObject {
        var name = ""
        var gender = 0
        var birthdate = ""
        var email = ""
        var password = ""
        var contactNo = ""
        var location = ""
        var image: String? = null
        fun getGender(): String {
            return when (gender) {
                1 -> "m"
                2 -> "f"
                else -> ""
            }
        }
    }
}