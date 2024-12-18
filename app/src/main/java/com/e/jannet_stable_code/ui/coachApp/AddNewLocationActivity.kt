package com.e.jannet_stable_code.ui.coachApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityAddNewLocationBinding
import com.e.jannet_stable_code.retrofit.controller.AddNewLocationController
import com.e.jannet_stable_code.retrofit.controller.IAddNewLocationController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IAddNewLocationView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddNewLocationActivity : BaseActivity(), OnMapReadyCallback, IAddNewLocationView {

    lateinit var controller: IAddNewLocationController
    override fun getController(): IBaseController? {
        return null
    }
    private lateinit var binding: ActivityAddNewLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.activity_add_new_location)
        binding = ActivityAddNewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        controller = AddNewLocationController(this, this)
        setTopBar()
        setData()

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

                showLoader()
                controller.callAddNewLocationApi(
                    id,
                    token,
                    binding.etxtLocation.text.trim().toString(),
                    "22.25458",
                    "28.6547",
                    binding. etxtCoatNumber.text.trim().toString()
                )
            }

        }


    }


    var lat: Double = 0.0
    var long: Double = 0.0
    var address: String = ""

    private fun setData() {

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map_location) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (Constants.eventDetailTop == null) return

        val data = Constants.eventDetailTop

        val addresss = intent.getStringExtra("ADDRESS")
        val description = intent.getStringExtra("DESCRIPTION")
        val eventName = intent.getStringExtra("EVENT_NAME")

//        txtField1.text= address.toString()
//        txtField2.text=description.toString()

        lat = 12.22222
        long = 22.25211
        address = "Dummy Address"

    }

    private fun setTopBar() {
        binding.topbarAddLocation.imgBack.visibility = View.VISIBLE
        binding.topbarAddLocation.imgBack.setOnClickListener { finish() }
        binding.topbarAddLocation.txtTitle.text = "ADD LOCATION"
    }

    override fun onMapReady(p0: GoogleMap) {

        val placeLocation = LatLng(lat, long)
        p0.addMarker(
            MarkerOptions().position(placeLocation)
                .title(address)
        )
        p0.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
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

}