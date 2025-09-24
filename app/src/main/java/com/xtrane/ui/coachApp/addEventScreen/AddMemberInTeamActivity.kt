package com.xtrane.ui.coachApp.addEventScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.xtrane.adapter.MemberListAdapter
import com.xtrane.adapter.MemberListAdapter.IDeleteTeamMemberClickListner
import com.xtrane.databinding.ActivityAddMemberInTeamBinding
import com.xtrane.retrofit.controller.DeleteMemberController
import com.xtrane.retrofit.controller.GetTeamMemberController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IDeleteMemberController
import com.xtrane.retrofit.controller.IGetTeamMemberController
import com.xtrane.retrofit.nonparticipantdata.GetMemberResult
import com.xtrane.retrofit.nonparticipantdata.NonParticipanResult
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IDeleteTeamView
import com.xtrane.viewinterface.IGetTeamMemberView

class AddMemberInTeamActivity : BaseActivity(), IGetTeamMemberView, IDeleteTeamMemberClickListner,
    IDeleteTeamView {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var deleteController: IDeleteMemberController
    lateinit var controller: IGetTeamMemberController
    private lateinit var binding: ActivityAddMemberInTeamBinding
    var event_id: String? = null
    var team_id: String? = null
    var id: String? = null
    var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  setContentView(R.layout.activity_add_member_in_team)
        binding = ActivityAddMemberInTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeData = StoreUserData(this)

        id = storeData.getString(Constants.COACH_ID)
        token = storeData.getString(Constants.COACH_TOKEN)

        if (intent.hasExtra("EVENT_ID")) {
            event_id = intent.getStringExtra("EVENT_ID")

        }

        if (intent.hasExtra("TEAM_ID")) {
            team_id = intent.getStringExtra("TEAM_ID")

        }


        binding.participantsList.txtTitle.text = "MEMBER LIST"
        binding.participantsList.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }


        controller = GetTeamMemberController(this, this)
        controller.callITeamMember(id!!, token!!, event_id!!)

        //  showLoader()

        binding.txtAddParticipants.setOnClickListener {

            val intent = Intent(this, AddParticipantActivity::class.java)
            intent.putExtra("EVENT_ID", event_id.toString())
            intent.putExtra("TEAM_ID", team_id.toString())
            startActivity(intent)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onGetTeamMember(response: List<GetMemberResult.MemberResult?>?) {
        hideLoader()
        val TeamAdapger = MemberListAdapter(this, response!!,this)
        binding.rvParticipantListInTeam.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()
    }

    override fun deleteTeamSuccessful() {
        controller = GetTeamMemberController(this, this)
        controller.callITeamMember(id!!, token!!, event_id!!)
    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }

    override fun deleteMember(deleteMatch: GetMemberResult.MemberResult) {
        deleteController = DeleteMemberController(this, this)
        deleteController.callDeleteMemberApi(
            id.toString(),
            token.toString(),
            deleteMatch.getId().toString(),
            event_id.toString(),
            team_id.toString()
        )
    }


}