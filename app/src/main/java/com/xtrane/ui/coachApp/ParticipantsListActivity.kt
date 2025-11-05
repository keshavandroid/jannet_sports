package com.xtrane.ui.coachApp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.xtrane.R
import com.xtrane.adapter.CoachListAapter1
import com.xtrane.adapter.MainTeamNonParticipantAapter
import com.xtrane.adapter.NonParticipentSelectionListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityParticipantsListBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.EventDetailController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.parentsApp.TeamsActivity
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
    var showbtn: String? ="no"
    var TAG: String? = "ParticipantList"
    var eventdata: EventDetailResponse? = null

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
        if (intent.hasExtra("showbtn")) {
            showbtn = intent.getStringExtra("showbtn")
            Log.e("showbtn", "======$showbtn")
        }
       // showLoader()

        if (showbtn.equals("yes"))
        {
            binding.txtCreateTeam.visibility=View.VISIBLE
            binding.txtTimer.visibility=View.VISIBLE

            val timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val seconds = (millisUntilFinished / 1000).toInt()
                    val minutes = seconds / 60
                    val remainingSeconds = seconds % 60

                    val formattedTime = String.format("%02d:%02d", minutes, remainingSeconds)
                    binding.txtTimer.text = formattedTime
                }

                override fun onFinish() {
                    Log.d("TIMER", "Timer finished!")
                    finish()

                }
            }
            timer.start()
        }
        else
        {
            binding.txtTimer.visibility=View.GONE

        }

        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, eventId.toString())
       // getEventDetails()

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
         //   if ()
            val intent = Intent(this, TeamsActivity::class.java)
            intent.putExtra("EVENT_ID", eventId.toString())
            intent.putExtra("eventdetailresponse", eventdata!!)
            startActivity(intent)
        }
    }

    private fun setTopBar() {
        binding.participantsListSelection.imgBack.visibility = View.VISIBLE
        binding.participantsListSelection.imgBack.setOnClickListener { finish() }
        binding.participantsListSelection.txtTitle.text = getString(R.string.participants)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {

       // hideLoader()

        val TeamAdapger = MainTeamNonParticipantAapter(this, response!!,"")
        binding.rvParticipantListInTeamMain.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()

        getEventDetails()

    }

    override fun onFail(message: String?, e: Exception?) {

       // hideLoader()
        showToast(message)
        getEventDetails()

    }
    private fun getEventDetails() {
        Log.e(TAG, "getEventDetails ==========$eventId")

        if (eventId != null &&
            !eventId.equals("null") &&
            !eventId.equals("")
        ) {
            EventDetailController(this@ParticipantsListActivity, object : ControllerInterface {
                override fun onFail(error: String?) {

                    Log.e(TAG, "onFail: fail ==========$error")
                }

                override fun <T> onSuccess(response: T, method: String) {
                    try {

                        val resp = response as EventDetailResponse

                        //setData(resp.getResult()!!)
                        Log.e(TAG, "getEventDetails1 ==========$resp")

                        eventdata = resp

                        Log.e(TAG, "onSuccess: event detil success===${resp.getResult()}")

                        Log.e(
                            "EventDetail",
                            "=========main iamge===${
                                resp.getResult()!![0]?.getMainimage().toString()
                            }"
                        )


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).callApi(eventId!!)
        }

    }

}