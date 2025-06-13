package com.xtrane.ui.parentsApp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xtrane.R
import com.xtrane.databinding.ActivityVenueBinding
import com.xtrane.utils.Constants
import com.xtrane.utils.Constants.eventDetailTop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale


class VenueActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityVenueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        Log.d(TAG, "onCreate: test447>>" + intent.getStringExtra("eventId"))
        if (intent.getStringExtra("eventId") != null &&
            !intent.getStringExtra("eventId").equals("null") &&
            !intent.getStringExtra("eventId").equals("")
        ) {

            var latitude = intent.getStringExtra(Constants.LATITUDE)
            var longitude = intent.getStringExtra(Constants.LONGITUDE)

            if (!latitude.isNullOrEmpty())
                lat = latitude.toDouble()

            if (!longitude.isNullOrEmpty())
                long = longitude.toDouble()

            if (intent.getStringExtra("ADDRESS") != null) {
                val addres = intent.getStringExtra("ADDRESS").toString()
                val latlon: LatLng = getLatLongFromAddress(this, addres)!!
                lat = latlon.latitude
                long = latlon.longitude

            }

        }


        setData()
    }

    var lat: Double = 0.0
    var long: Double = 0.0
    var address: String = ""

    private fun setData() {

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (eventDetailTop == null) return

        val data = eventDetailTop

        val addresss = intent.getStringExtra("ADDRESS")
        val description = intent.getStringExtra("DESCRIPTION")
        val eventName = intent.getStringExtra("EVENT_NAME")

        binding.txtField2.text = eventName
//        txtField2.text=description.toString()
        address = addresss.toString()

        binding.txtField1.text = addresss
    }

    private fun setTopBar() {
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text = getString(R.string.venue1)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        Log.d(TAG, "onMapReady: lat $lat long= $long")
        val placeLocation = LatLng(lat, long)

        googleMap.addMarker(
            MarkerOptions().position(placeLocation)
                .title(address)
        )

        val location = CameraUpdateFactory.newLatLngZoom(
            placeLocation, 15f
        )
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

fun getLatLongFromAddress(context: Context, addressString: String): LatLng? {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocationName(addressString, 1)

        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val latitude = address.latitude
            val longitude = address.longitude
            return LatLng(latitude, longitude)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}