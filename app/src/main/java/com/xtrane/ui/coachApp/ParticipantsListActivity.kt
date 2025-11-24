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
import com.xtrane.retrofit.controller.AddMemberToTeamController
import com.xtrane.retrofit.controller.EventDetailController
import com.xtrane.retrofit.controller.GetTeamMemberController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IGetTeamMemberController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.ITeamListController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.controller.TeamListController
import com.xtrane.retrofit.nonparticipantdata.GetMemberResult
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.retrofit.teamlistdata.TeamListResult
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.parentsApp.EventDetailsActivity
import com.xtrane.ui.parentsApp.TeamsActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IGetTeamMemberView
import com.xtrane.viewinterface.INonParticipanView
import com.xtrane.viewinterface.ITeamListView
import com.xtrane.viewinterface.RegisterControllerInterface


class ParticipantsListActivity : BaseActivity(), INonParticipanView, IGetTeamMemberView,
    ITeamListView,
    NonParticipentSelectionListAdapter.ISelectCheckBoxListner, RegisterControllerInterface {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var teamcontroller: ITeamListController

    lateinit var controller: INonParticipantController
    private lateinit var binding: ActivityParticipantsListBinding
    var eventId: String? = null
    var showbtn: String? = "no"
    var TAG: String? = "ParticipantList"
    var eventdata: EventDetailResponse? = null
    var selectedMemberID: String? = null

    lateinit var controllerAddTeam: AddMemberToTeamController
    var myTeam: TeamListResult? = null
    var id: String = ""
    var token: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_participants_list)
        binding = ActivityParticipantsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeData = StoreUserData(this)
        id = storeData.getString(Constants.COACH_ID)
        token = storeData.getString(Constants.COACH_TOKEN)


        if (intent.hasExtra("eventId")) {
            eventId = intent.getStringExtra("eventId")
            Log.e("eventID", "======$eventId")
        }
        if (intent.hasExtra("showbtn")) {
            showbtn = intent.getStringExtra("showbtn")
            Log.e("showbtn", "======$showbtn")
        }
        // showLoader()

        if (showbtn.equals("yes")) {

            binding.txtCreateTeam.visibility = View.VISIBLE
            binding.txtTimer.visibility = View.VISIBLE

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
        else {
            binding.txtTimer.visibility = View.GONE
            binding.txtCreateTeam.visibility = View.GONE

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
        binding.txtCreateTeam.text = "Participant Selection"
        binding.txtCreateTeam.setOnClickListener {
            //   if ()
//            val intent = Intent(this, TeamsActivity::class.java)
//            intent.putExtra("EVENT_ID", eventId.toString())
//            intent.putExtra("eventdetailresponse", eventdata!!)
//            startActivity(intent)

            if (myTeam != null && myTeam!!.getTeamId() != null && myTeam!!.getTeamId()!! > 0) {
                if (selectedMemberID != null && selectedMemberID!!.length > 0) {
                    controllerAddTeam = AddMemberToTeamController(this, this)
                    controllerAddTeam.addTeamMember(
                        eventId.toString(),
                        selectedMemberID!!,
                        myTeam!!.getTeamId().toString()
                    )

                } else {
                    showToast("Please select member")
                }

            } else {
                showToast("Team not found")
            }

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

        val TeamAdapger = MainTeamNonParticipantAapter(response!!,showbtn.toString(), this)
        binding.rvParticipantListInTeamMain.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()

        teamcontroller = TeamListController(this, this)
        teamcontroller.callTeamLostApi(id, token, eventId.toString())
        //   getEventDetails()

    }

    override fun showLoader() {
        // TODO("Not yet implemented")
    }

    override fun showLoader(message: String?) {
        //    TODO("Not yet implemented")
    }

    override fun hideLoader() {
        //  TODO("Not yet implemented")
    }

    override fun onFail(message: String?, e: Exception?) {

        // hideLoader()
        showToast(message)
        //   getEventDetails()

    }

//    private fun getEventDetails() {
//        Log.e(TAG, "getEventDetails ==========$eventId")
//
//        if (eventId != null &&
//            !eventId.equals("null") &&
//            !eventId.equals("")
//        ) {
//            EventDetailController(this@ParticipantsListActivity, object : ControllerInterface {
//                override fun onFail(error: String?) {
//
//                    Log.e(TAG, "onFail: fail ==========$error")
//                }
//
//                override fun <T> onSuccess(response: T, method: String) {
//                    try {
//
//                        val resp = response as EventDetailResponse
//
//                        //setData(resp.getResult()!!)
//                        Log.e(TAG, "getEventDetails1 ==========$resp")
//
//                        eventdata = resp
//
//                        Log.e(TAG, "onSuccess: event detil success===${resp.getResult()}")
//
//                        Log.e(
//                            "EventDetail",
//                            "=========main iamge===${
//                                resp.getResult()!![0]?.getMainimage().toString()
//                            }"
//                        )
//
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }).callApi(eventId!!)
//        }
//
//    }

    override fun onCheckBoxClick(memberId: String, isSelected: Boolean) {
        selectedMemberID = memberId
    }

    override fun <T> onSuccess(response: T) {
        showToast(response.toString())

        startActivity(
            Intent(this, EventDetailsActivity::class.java).putExtra(
                "from",
                "coachPersonal"
            ).putExtra("eventId", eventId.toString())


        )
    }

    override fun onFail(error: String?) {
        // TODO("Not yet implemented")
    }

    override fun <T> onSuccessNew(response: T, method: String) {
        //TODO("Not yet implemented")
    }

    override fun onGetTeamMember(response: List<GetMemberResult.MemberResult?>?) {

    }

    override fun onTeamListSuccess(response: List<TeamListResult?>?) {
        val storeData = StoreUserData(this)
        val coachId = storeData.getString(Constants.COACH_ID)

        Log.e("onTeamListSuccess_coachId1",coachId.toString()+"==")
        if (!response.isNullOrEmpty()) {
            for (team in response) {
                if (team?.getCoachID().toString().equals(coachId)) {
                    Log.e("onTeamListSuccess_coachId2",team?.getCoachID().toString()+"==")

                    myTeam = team
                    break
                }
            }
        }
    }
}