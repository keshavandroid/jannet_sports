package com.xtrane.ui.coachApp.addEventScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.xtrane.R
import com.xtrane.adapter.NonParticipentSelectionListAdapter
import com.xtrane.databinding.ActivityAddParticipantBinding
import com.xtrane.databinding.ActivityVenueBinding
import com.xtrane.retrofit.controller.AddMemberToTeamController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.INonParticipantController
import com.xtrane.retrofit.controller.NonParticipantController
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.INonParticipanView
import com.xtrane.viewinterface.RegisterControllerInterface


class AddParticipantActivity : BaseActivity(), INonParticipanView, RegisterControllerInterface,
    NonParticipentSelectionListAdapter.ISelectCheckBoxListner {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: INonParticipantController
    lateinit var controllerAddTeam: AddMemberToTeamController
    var tempList = ArrayList<NonParticipanResult>()
    private lateinit var binding: ActivityAddParticipantBinding
    var event_id: String? = null
    var team_id: String? = null
    var selectedMemberID: String? = null
    var TeamAdapger: NonParticipentSelectionListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_add_participant)
        binding = ActivityAddParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.participantsListSelection.txtTitle.text = "ADD MEMBER"
        binding.participantsListSelection.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }


        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)


        if (intent.hasExtra("EVENT_ID")) {
            event_id = intent.getStringExtra("EVENT_ID")

        }
        if (intent.hasExtra("TEAM_ID")) {
            team_id = intent.getStringExtra("TEAM_ID")

        }


        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, "155")

        showLoader()

        binding.txtSelectParticipants.setOnClickListener {
            // Call AddMemberController here
            // For example, if you have an AddMemberController instance:

            controllerAddTeam = AddMemberToTeamController(this, this)
            controllerAddTeam.addTeamMember(event_id.toString(), selectedMemberID!!, team_id.toString())

//            val intent = Intent(this, AddMemberInTeamActivity::class.java) // Replace with your actual activity
//            startActivity(intent)
//            finish() // Finish current activity if needed
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {


        hideLoader()
        tempList = ArrayList<NonParticipanResult>()
        for (nonParticipanList in response!!) {


            if (nonParticipanList?.getSelected() == true) {

                tempList.add(nonParticipanList)
                Log.e("TAG", "onNonParticipantSuccess: $tempList")
            } else {


            }

        }


        TeamAdapger = NonParticipentSelectionListAdapter(this, response,this)
        binding.rvParticipantListInTeamSelection.adapter = TeamAdapger
        TeamAdapger!!.notifyDataSetChanged()


    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }

    override fun <T> onSuccess(response: T) {
    }

    override fun onFail(error: String?) {
    }

    override fun <T> onSuccessNew(response: T, method: String) {
       // TODO("Not yet implemented")
    }


    override fun onCheckBoxClick(memberId: String, isSelected: Boolean) {
        selectedMemberID=memberId
    }
}