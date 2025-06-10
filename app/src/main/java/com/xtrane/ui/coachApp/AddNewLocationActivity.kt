package com.xtrane.ui.coachApp

import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.xtrane.R
import com.xtrane.databinding.ActivityAddNewLocationBinding
import com.xtrane.retrofit.controller.AddNewLocationController
import com.xtrane.retrofit.controller.IAddNewLocationController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IAddNewLocationView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class AddNewLocationActivity : BaseActivity(), OnMapReadyCallback, IAddNewLocationView {

    lateinit var controller: IAddNewLocationController
    override fun getController(): IBaseController? {
        return null
    }

    private lateinit var binding: ActivityAddNewLocationBinding
    var latitude = "25.761681"
    var longitude = "-80.191788"
    var address = ""

    lateinit var gmap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   setContentView(R.layout.activity_add_new_location)
        binding = ActivityAddNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        controller = AddNewLocationController(this, this)
        setTopBar()
        setData()

//        binding.etxtLocation.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Optional: Actions before text is changed
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val locationName = s.toString().trim()
//                address = locationName
//                if (locationName.length > 3) { // Fetch lat-long for input longer than 3 characters
//                    val latLong = getLatLongFromAddress(locationName)
//                    latLong?.let {
//                        Log.d("LatLong", "Latitude: ${it.first}, Longitude: ${it.second}")
//
//                        latitude = it.first.toString()
//                        longitude = it.second.toString()
//
//                    }
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                // Optional: Actions after text is changed
//            }
//        })

        binding.etxtLocation.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val locationName = textView.text.toString().trim()
                if (locationName.isNotEmpty()) {
                    address = locationName
                    if (locationName.length > 3) { // Fetch lat-long for input longer than 3 characters
                        val latLong = getLatLongFromAddress(locationName)
                        latLong?.let {
                            Log.d("LatLong", "Latitude: ${it.first}, Longitude: ${it.second}")
                            latitude = it.first.toString()
                            longitude = it.second.toString()
                            val placeLocation = LatLng(latitude.toDouble(), longitude.toDouble())
                            gmap.addMarker(MarkerOptions().position(placeLocation).title(address))
                            gmap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
                            gmap.animateCamera(CameraUpdateFactory.zoomTo(10F), 1000, null);
                        }
                    }
                } else {
                    Log.e("Location", "Input is empty")
                }
                true // Return true to indicate the action has been handled
            } else {
                false // Return false to let the system handle other actions
            }
        }

        binding.txtAddNewLocation.setOnClickListener {

            val stordata = StoreUserData(this)
            val id = stordata.getString(Constants.COACH_ID)
            val token = stordata.getString(Constants.COACH_TOKEN)

            if (binding.etxtLocation.text.trim().isEmpty()) {

                showToast("Add Location Address")
            } else {
//            else if (etxt_coat_number.text.trim().isEmpty()){
//
//                showToast("Select Coat Number")
//            }
                val latLong = getLatLongFromAddress(binding.etxtLocation.text.toString())
                latLong?.let {
                    Log.d("LatLong", "Latitude: ${it.first}, Longitude: ${it.second}")
                    latitude = it.first.toString()
                    longitude = it.second.toString()
                }
                showLoader()

                controller.callAddNewLocationApi(
                    id,
                    token,
                    binding.etxtLocation.text.trim().toString(),
                    latitude,
                    longitude,
                    binding.etxtCoatNumber.text.trim().toString()
                )
            }

        }


    }


//    var lat: Double = 0.0
//    var long: Double = 0.0
//    var address: String = ""

    private fun setData() {

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map_location) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (Constants.eventDetailTop == null)
            return

//        val data = Constants.eventDetailTop
//
//        val addresss = intent.getStringExtra("ADDRESS")
//        val description = intent.getStringExtra("DESCRIPTION")
//        val eventName = intent.getStringExtra("EVENT_NAME")
//
////        txtField1.text= address.toString()
////        txtField2.text=description.toString()
//
//        lat = latitude
//        long = longitude
//        address = "Dummy Address"

    }

    private fun setTopBar() {
        binding.topbarAddLocation.imgBack.visibility = View.VISIBLE
        binding.topbarAddLocation.imgBack.setOnClickListener { finish() }
        binding.topbarAddLocation.txtTitle.text = "ADD LOCATION"
    }

    override fun onMapReady(p0: GoogleMap) {
        gmap=p0

        gmap.uiSettings.isZoomControlsEnabled = true
        gmap.uiSettings.isZoomGesturesEnabled = true

        val placeLocation = LatLng(latitude.toDouble(), longitude.toDouble())
        gmap.addMarker(MarkerOptions().position(placeLocation).title(address))
        gmap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        gmap.animateCamera(CameraUpdateFactory.zoomTo(10F), 1, null);

//        p0.mapType = GoogleMap.MAP_TYPE_NORMAL;
//        try {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            p0.isMyLocationEnabled = true;
//        } catch (e:Exception) {
//            e.printStackTrace()
//        }
//
//        p0.isTrafficEnabled = true;
//        p0.isIndoorEnabled = true;
//        p0.isBuildingsEnabled = true;
//        p0.uiSettings.isZoomControlsEnabled = true;

//        Log.d(TAG, "onMapReady: test84>>")


    }

    override fun onLocationAddedSuccessful() {

        hideLoader()
        showToast("New Location Add Successful")

        onBackPressed()
        finish()

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }

    private fun getLatLongFromAddress(address: String): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(address, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                Pair(latitude, longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}