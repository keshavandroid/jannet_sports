package com.xtrane.ui.coachApp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xtrane.R
import com.xtrane.adapter.CoachListAapter1
import com.xtrane.adapter.MainTeamNonParticipantAapter
import com.xtrane.adapter.NonParticipentSelectionListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityParticipantsListBinding
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachParticipantController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.INonParticipanView


class CoachListActivity : BaseActivity() {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: ICoachParticipantController
    private lateinit var binding: ActivityParticipantsListBinding
    var eventId: String? =null
    lateinit var eventdata: EventDetailResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_participants_list)

        binding = ActivityParticipantsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        if (intent.hasExtra("eventId"))
        {
            eventId = intent.getStringExtra("eventId")
            Log.e("eventID","======$eventId")
        }
        if (intent.hasExtra("eventdetailresponse"))
        {
            eventdata = intent.getSerializableExtra("eventdetailresponse") as EventDetailResponse
            Log.e("eventdata=", eventdata.getResult()!!.size.toString())
        }

        setTopBar()

        for (i in 0 until eventdata.getResult()!!.size) {
                val coachadpter = CoachListAapter1(this,
                    eventdata.getResult()!![i]!!.getCoachArray()!!
                )
                binding.rvParticipantListInTeamMain.adapter = coachadpter
        }

        binding.txtCreateTeam.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CreateTeamActivity::class.java
                )
            )
        }
    }

    private fun setTopBar() {
        binding.participantsListSelection.imgBack.visibility = View.VISIBLE
        binding.participantsListSelection.imgBack.setOnClickListener { finish() }
        binding.participantsListSelection.txtTitle.text = getString(R.string.coach)
    }

}