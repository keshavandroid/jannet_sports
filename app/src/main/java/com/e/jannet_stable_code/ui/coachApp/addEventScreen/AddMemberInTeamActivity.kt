package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MemberListAdapter
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.INonParticipantController
import com.e.jannet_stable_code.retrofit.controller.NonParticipantController
import com.e.jannet_stable_code.retrofit.nonparticipantdata.NonParticipanResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.INonParticipanView
import kotlinx.android.synthetic.main.activity_add_member_in_team.*
import kotlinx.android.synthetic.main.activity_added_team_list.*
import kotlinx.android.synthetic.main.topbar_layout.*

class AddMemberInTeamActivity : BaseActivity(), INonParticipanView {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: INonParticipantController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member_in_team)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        txtTitle.text = "MEMBER LIST"
        imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }

        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, "155")
        showLoader()
        txt_Add_Participants.setOnClickListener {

            val intent = Intent(this, AddParticipantActivity::class.java)
            startActivity(intent)


        }

    }

    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {

        hideLoader()
        var TeamAdapger = MemberListAdapter(this, response!!)
        rv_participant_list_in_team.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()





    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }
}