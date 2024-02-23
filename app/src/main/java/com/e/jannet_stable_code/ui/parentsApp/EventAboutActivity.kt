package com.e.jannet_stable_code.ui.parentsApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.SliderAdapterExample
import com.e.jannet_stable_code.retrofit.response.SliderItem
import com.e.jannet_stable_code.utils.Constants.eventDetailTop
import com.e.jannet_stable_code.utils.TAG
import com.e.jannet_stable_code.utils.Utilities
import kotlinx.android.synthetic.main.activity_event_about.*
import kotlinx.android.synthetic.main.topbar_layout.*

class EventAboutActivity : AppCompatActivity() {
    var adapter: SliderAdapterExample? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_about)

        setTopBar()

        adapter = SliderAdapterExample(this)
        imageSlider.setSliderAdapter(adapter!!)


        if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
                .equals("match")
        ) {

            imgEdit.isGone = true

        } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
                .equals("home")
        ) {

            imgEdit.isGone = true

        } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
                .equals("coachPersonal")
        ) {

            imgEdit.isVisible = true
        }



        setData()
//        EventDetailController(this, object : ControllerInterface {
//            override fun onFail(error: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun <T> onSuccess(response: T, method: String) {
//
//                try {
//
//                    val resp = response as EventDetailResponse
//                    val data = resp.getResult()!![0]
//                    val sportsNameS = ArrayList<String>()
//
//
//
//                    data!!.getSportsName()!!.forEach {
//                        it?.getSportsName()
//                        sportsNameS.add(it?.getSportsName().toString())
//                    }
//
//                    Log.e(TAG,"SportsList========$sportsNameS")
//
//                    txtEventName.text= data?.getEventName()
//                    txtEventDate.text=Utilities.convertDateFormat(data?.getEventDate()!!)
//                    txtCoachName.text= data.getCreatorName().toString()
//                    txtAgeRange.text=data.getAgeRange().toString()+" Age"
//                    txtSportsName.text=sportsNameS.toString()
//                    txtParticipantsCount.text= data.getParticipants() + " " + "Participants"
//                    txtGenderAccepted.text= data.getGenderApplicable().toString()
//                    txtDescription.text=data.getDescription().toString()
//                    txtFees.text="$"+data.getFees()
//
//                    addNewItem(
//                        "",
//                        data.getMainimage()!!
//                    )
//
//                    for(i in data.getImages()!!.indices) {
//                        addNewItem(
//                            "",
//                            data.getImages()!![i]?.getImage()!!
//                        )
//                    }
//
//
//
//                    Log.e("EventDetail","=========main iamge===${resp.getResult()!![0]?.getMainimage().toString()}")
//                    Log.e("EventDetail","=========images===${resp.getResult()!![0]?.getImages().toString()}")
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//
//        }).callApi(intent.getStringExtra("eventId")!!)
//
//        Log.e("AboutEvent","==========eid=${intent.getStringExtra("eventId")}")
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        if (eventDetailTop == null) return

        val data = eventDetailTop
        val sportsNameS = ArrayList<String>()

        data!!.getSportsName()!!.forEach {
            it?.getSportsName()
            sportsNameS.add(it?.getSportsName().toString())
        }

        Log.e(TAG, "SportsList========$sportsNameS")

        txtEventName.text = data?.getEventName()
        txtEventDate.text = Utilities.convertDateFormat(data?.getEventDate()!!)
        txtCoachName.text = data.getCreatorName().toString()
        txtAgeRange.text = data.getMinAge().toString() + "-" + data.getMaxAge().toString() + " Age"

        if (!sportsNameS.isNullOrEmpty())
            txtSportsName.text = android.text.TextUtils.join(",", sportsNameS)
        else
            txtSportsName.text = sportsNameS.toString()

        txtParticipantsCount.text = data.getParticipants() + " " + "Participants"
        txtGenderAccepted.text = data.getGenderApplicable().toString()
        txtDescription.text = data.getDescription().toString()
        txtFees.text = "$" + data.getFees()
//        txtGrade_about.text = data.getGrade().toString()

        try {
            txtGrade_about.text = data.getMinGrade().toString() +" - "+ data.getMaxGrade().toString() + " Grade"
        }catch (e: Exception){
            e.printStackTrace()
        }



        addNewItem(
            "",
            data.getMainimage()!!
        )

        for (i in data.getImages()!!.indices) {
            addNewItem(
                "",
                data.getImages()!![i]?.getImage()!!
            )
        }
    }

    private fun addNewItem(string1: String, string2: String) {
        val sliderItem = SliderItem()
        sliderItem.description = string1
        sliderItem.imageUrl = string2
        adapter!!.addItem(sliderItem)
        imageSlider.startAutoCycle()
    }

    private fun setTopBar() {
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.about1)
    }
}