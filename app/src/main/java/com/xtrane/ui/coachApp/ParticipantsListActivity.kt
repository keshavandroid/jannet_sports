package com.xtrane.ui.coachApp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xtrane.R
import com.xtrane.adapter.MainTeamNonParticipantAapter
import com.xtrane.adapter.NonParticipentSelectionListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityParticipantsListBinding
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.INonParticipanView


class ParticipantsListActivity : BaseActivity(), INonParticipanView {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: INonParticipantController
    private lateinit var binding: ActivityParticipantsListBinding
    var eventId: String? =null
    var showbtn: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_participants_list)
        binding = ActivityParticipantsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)


        if (intent.hasExtra("eventId"))
        {
            eventId = intent.getStringExtra("eventId")
            Log.e("eventID","======$eventId")
        }

        showLoader()

        if (showbtn.equals("y"))
        {
            binding.txtCreateTeam.visibility=View.VISIBLE
            binding.txtTimer.visibility=View.VISIBLE
        }
        else
        {
            binding.txtTimer.visibility=View.GONE

        }
        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, eventId.toString())

//        if(intent.getStringExtra("from").equals("teamsDetailActivity")){
//            txtWait.visibility=View.VISIBLE
//            llWait1.visibility=View.VISIBLE
//            llWait2.visibility=View.VISIBLE
//            llWait3.visibility=View.VISIBLE
//            llWait4.visibility=View.VISIBLE
//            txtCreateTeam.visibility=View.GONE
//        }else{
//            txtWait.visibility=View.GONE
//            llWait1.visibility=View.GONE
//            llWait2.visibility=View.GONE
//            llWait3.visibility=View.GONE
//            llWait4.visibility=View.GONE
//            txtCreateTeam.visibility=View.GONE
//        }

        setTopBar()
        binding.txtCreateTeam.text="Participant Selection"
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
        binding.participantsListSelection.txtTitle.text = getString(R.string.participants)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {

        hideLoader()

        val TeamAdapger = MainTeamNonParticipantAapter(this, response!!,"")
        binding.rvParticipantListInTeamMain.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
    }
}