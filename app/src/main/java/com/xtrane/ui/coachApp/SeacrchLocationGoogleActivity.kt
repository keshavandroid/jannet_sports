package com.xtrane.ui.coachApp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.xtrane.R
import com.xtrane.adapter.BookingListAdapter
import com.xtrane.adapter.LocationListAdapter

class SeacrchLocationGoogleActivity : AppCompatActivity() {

   /* val placesApi = PlaceAPI.Builder()
        .apiKey("AIzaSyDZz4wntacjlvQ3aEGb9eWpDPU6M71t_Vc")*//* getString(R.string.Google_API_key)*//*
        .build(this@SeacrchLocationGoogleActivity)*/
    var autoCompleteEditText: AutoCompleteTextView? = null
    var iv_clear: ImageView? = null
    var tv_cancel: TextView? = null
    var rv_location: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seacrch_location_google)
        autoCompleteEditText = findViewById(R.id.autoCompleteEditText)
        iv_clear = findViewById(R.id.iv_clear)
        tv_cancel = findViewById(R.id.tv_cancel)
        rv_location = findViewById(R.id.rv_location)
        iv_clear!!.setOnClickListener(View.OnClickListener { autoCompleteEditText!!.setText("") })
        tv_cancel!!.setOnClickListener(View.OnClickListener { finish() })


       /* autoCompleteEditText!!.setAdapter(PlacesAutoCompleteAdapter(this, placesApi))
        autoCompleteEditText!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val place =
                    parent.getItemAtPosition(position) as `in`.madapps.placesautocomplete.model.Place
                autoCompleteEditText!!.setText(place.description)
                getPlaceDetails(place.id)
            }*/
       /* var adapter = LocationListAdapter(this,placesApi)
        rv_location!!.adapter =LocationListAdapter(this, placesApi)*/
    }



    private fun getPlaceDetails(placeId: String) {
       /* placesApi.fetchPlaceDetails(placeId, object :
            OnPlacesDetailsListener {
            override fun onError(errorMessage: String) {
                Toast.makeText(this@SeacrchLocationGoogleActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPlaceDetailsFetched(placeDetails: PlaceDetails) {
                setupUI(placeDetails)
            }
        })*/
    }



}