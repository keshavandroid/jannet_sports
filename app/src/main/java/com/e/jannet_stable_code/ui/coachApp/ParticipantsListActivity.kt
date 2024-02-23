package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MainTeamNonParticipantAapter
import com.e.jannet_stable_code.adapter.NonParticipentSelectionListAdapter
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.INonParticipantController
import com.e.jannet_stable_code.retrofit.controller.NonParticipantController
import com.e.jannet_stable_code.retrofit.nonparticipantdata.NonParticipanResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.INonParticipanView
import kotlinx.android.synthetic.main.activity_add_participant.*
import kotlinx.android.synthetic.main.activity_participants_list.*
import kotlinx.android.synthetic.main.topbar_layout.*

class ParticipantsListActivity : BaseActivity(), INonParticipanView {
    override fun getController(): IBaseController? {
        return null
    }

    lateinit var controller: INonParticipantController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participants_list)


        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)

        controller = NonParticipantController(this, this)
        controller.callNonParticipantApi(id, token, "155")
            showLoader()
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
        txtCreateTeam.setOnClickListener { startActivity(Intent(this,CreateTeamActivity::class.java)) }
    }

    private fun setTopBar() {
        imgBack.visibility= View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text=getString(R.string.participants)
    }

    override fun onNonParticipantSuccess(response: List<NonParticipanResult?>?) {

        hideLoader()

        var TeamAdapger = MainTeamNonParticipantAapter(this, response!!)
        rv_participant_list_in_team_main.adapter = TeamAdapger
        TeamAdapger.notifyDataSetChanged()

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
    }
}