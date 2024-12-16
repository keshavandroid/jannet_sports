package com.e.jannet_stable_code.ui.loginRegister.registerCoachScreen

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.ui.coachApp.SeacrchLocationGoogleActivity
import com.e.jannet_stable_code.ui.loginRegister.loginScreen.LoginActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.DatePickerResult
import com.e.jannet_stable_code.utils.PickImage
import com.e.jannet_stable_code.utils.TAG
import com.e.jannet_stable_code.utils.Utilities
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
    private val PERMISSION_REQUEST_CODE = 1001

    var model: RegisterCoachModel? = null
    var API_KEY = "AIzaSyDZz4wntacjlvQ3aEGb9eWpDPU6M71t_Vc"
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

            if (pickImage != null && pickImage!!.getImage() != null) {
                data.image = pickImage?.getImage()
            }

            data.name = etxtName.text.toString().trim()
            data.gender = genderFlag
            data.birthdate = txtBDate.text.toString().trim()
            data.email = etxtEmail.text.toString().trim()
            data.password = etxtPwd.text.toString().trim()
            data.contactNo = etxtPhNo.text.toString().trim() + etxtPhNo1.text.toString()
                .trim() + etxtPhNo2.text.toString().trim()
            data.location = "*"
            //etxtLocation.text.toString().trim()

            val validData = model?.registerValidation(data)

            if (validData != null && validData) {
                model?.registerCoach()
            }

        }
        imgProfile.setOnClickListener {

            if (isPermissionGranted()) {
                Utilities.showLog(TAG, "isPermissionGranted1:=" + isPermissionGranted().toString())

                pickImage = PickImage(this@RegisterCoachActivity)

            } else {
                Utilities.showLog(TAG, "isPermissionGranted2:=")

                requestPermission()

            }
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

    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        }
        else
        {
            return ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }

    }

    // Request the permission
    private fun requestPermission() {
        Utilities.showLog(TAG, "requestPermission1:=")
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Utilities.showLog(TAG, "greater than 13=")



            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }


        } else {
            Utilities.showLog(TAG, "less than 13=")
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            // For older versions
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
//            }
        }
        Utilities.showLog(TAG, "permissions:=" + permissions.toString())

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Utilities.showLog(TAG, "PERMISSION_REQUEST_CODE:=" + PERMISSION_REQUEST_CODE)
        Utilities.showLog(TAG, "requestCode:=" + requestCode)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            Utilities.showLog(TAG, "grantResults:=" + grantResults[0] + "==")
            Utilities.showLog(TAG, "PERMISSION_GRANTED:=" + PackageManager.PERMISSION_GRANTED)

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
                pickImage = PickImage(this@RegisterCoachActivity)
            } else {
                Utilities.showLog(TAG, "grantResults:=" + "denied==")

                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}