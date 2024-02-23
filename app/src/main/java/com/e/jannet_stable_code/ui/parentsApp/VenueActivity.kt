package com.e.jannet_stable_code.ui.parentsApp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityVenueBinding
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.Constants.eventDetailTop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.topbar_layout.*


class VenueActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityVenueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        Log.d(TAG, "onCreate: test447>>" + intent.getStringExtra("eventId"))
        if (intent.getStringExtra("eventId") != null &&
            !intent.getStringExtra("eventId").equals("null") &&
            !intent.getStringExtra("eventId").equals("")) {

            var latitude = intent.getStringExtra(Constants.LATITUDE)
            var longitude = intent.getStringExtra(Constants.LONGITUDE)

            if (!latitude.isNullOrEmpty())
                lat = latitude.toDouble()

            if (!longitude.isNullOrEmpty())
                long = longitude.toDouble()

        }


        setData()
    }

    var lat:Double=0.0
    var long:Double=0.0
    var address:String=""

    private fun setData() {

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(eventDetailTop==null) return

        val data= eventDetailTop

        val addresss  = intent.getStringExtra("ADDRESS")
        val description = intent.getStringExtra("DESCRIPTION")
        val eventName = intent.getStringExtra("EVENT_NAME")

        binding.txtField2.text = eventName
//        txtField2.text=description.toString()
        address = addresss.toString()

        binding.txtField1.text= addresss
    }

    private fun setTopBar() {
        imgBack.visibility= View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text=getString(R.string.venue1)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        Log.d(TAG, "onMapReady: lat $lat long= $long")
        val placeLocation = LatLng(lat,long)
        googleMap.addMarker(MarkerOptions().position(placeLocation)
            .title(address))
        val location = CameraUpdateFactory.newLatLngZoom(
            placeLocation, 15f)
        googleMap.animateCamera(location)

//        p0.animateCamera(CameraUpdateFactory.zoomTo(10F), 1000, null);

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
    private val TAG = "VenueActivity"
}