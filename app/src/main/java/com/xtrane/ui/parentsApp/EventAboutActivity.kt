package com.xtrane.ui.parentsApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xtrane.R
import com.xtrane.adapter.SliderAdapterExample
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityEventAboutBinding
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.retrofit.response.SliderItem
import com.xtrane.ui.coachApp.CoachListActivity
import com.xtrane.ui.coachApp.ParticipantsListActivity
import com.xtrane.utils.Constants.eventDetailTop
import com.xtrane.utils.TAG
import com.xtrane.utils.Utilities


class EventAboutActivity : AppCompatActivity() {
    var adapter: SliderAdapterExample? = null
    private lateinit var binding: ActivityEventAboutBinding
    lateinit var eventdata: EventDetailResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_event_about)
        binding = ActivityEventAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        adapter = SliderAdapterExample(this)
        binding.imageSlider.setSliderAdapter(adapter!!)


        if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
                .equals("match")
        )
        {
            binding.topbar.imgEdit.isGone = true
        }
        else if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("home"))
        {
            binding.topbar.imgEdit.isGone = true
        }
        else if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("coachPersonal"))
        {
            binding.topbar.imgEdit.isVisible = true
        }

        if (intent.hasExtra("eventdetailresponse")) {
            eventdata= intent.getSerializableExtra("eventdetailresponse") as EventDetailResponse
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

        binding.txtEventName.text = data?.getEventName()
        binding.txtEventDate.text = Utilities.convertDateFormat(data?.getEventDate()!!)
        binding.txtCoachName.text = data.getCreatorName().toString()
        binding.txtAgeRange.text =
            data.getMinAge().toString() + "-" + data.getMaxAge().toString() + " Age"

        if (!sportsNameS.isNullOrEmpty())
            binding.txtSportsName.text = android.text.TextUtils.join(",", sportsNameS)
        else
            binding.txtSportsName.text = sportsNameS.toString()

        binding.txtParticipantsCount.text = data.getParticipants() + " " + "Participants"
        binding.txtGenderAccepted.text = data.getGenderApplicable().toString()
        binding.txtDescription.text = data.getDescription().toString()
        binding.txtFees.text = "$" + data.getFees()
//        txtGrade_about.text = data.getGrade().toString()

        try {
            binding.txtGradeAbout.text =
                data.getMinGrade().toString() + " - " + data.getMaxGrade().toString() + " Grade"
        } catch (e: Exception) {
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

        binding.txtViewparticipant.setOnClickListener {

            Log.e("participant=EventID=", data.getId().toString())
            startActivity(
                Intent(
                    this,
                    ParticipantsListActivity::class.java
                ).putExtra("eventId", data.getId().toString())
            )
        }
        binding.txtViewCoach.setOnClickListener {

            startActivity(Intent(this, CoachListActivity::class.java
                ).putExtra("eventdetailresponse", eventdata)
            )
        }
    }

    private fun addNewItem(string1: String, string2: String) {
        val sliderItem = SliderItem()
        sliderItem.description = string1
        sliderItem.imageUrl = string2
        adapter!!.addItem(sliderItem)
        binding.imageSlider.startAutoCycle()
    }

    private fun setTopBar() {
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text = getString(R.string.about1)
    }
}