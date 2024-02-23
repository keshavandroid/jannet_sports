package com.e.jannet_stable_code.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.TeamListAdapter
import com.e.jannet_stable_code.adapter.TeamListEDAdapter
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.AddTeamsFinalActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.TeamDetailActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.TeamsDetailAndAddParticipentActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteTeamView
import com.e.jannet_stable_code.viewinterface.ITeamListView
import kotlinx.android.synthetic.main.activity_added_team_list.*
import kotlinx.android.synthetic.main.activity_teams.*
import kotlinx.android.synthetic.main.topbar_layout.*

class TeamsActivity : BaseActivity(), ITeamListView, TeamListEDAdapter.IEditTeamClickListner,
    TeamListEDAdapter.IDeleteClickListner, IDeleteTeamView, TeamListEDAdapter.IItemClickListner {

    var id = ""
    var token = ""

    override fun getController(): IBaseController? {
        return null
    }

    lateinit var deleteTeamCOntroller: IDeleteTeamController
    lateinit var controller: ITeamListController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

//        setTopBar()


        var storeData = StoreUserData(this)

        if (storeData.getString(Constants.COACH_ID)
                .trim() == null || storeData.getString(Constants.COACH_ID).trim().isEmpty()
        ) {

            id = SharedPrefUserData(this).getSavedData().id
            token = SharedPrefUserData(this).getSavedData().token
            val event_id = intent.getStringExtra("EVENT_ID")


            controller = TeamListController(this, this)
            controller.callTeamLostApi(id, token, event_id.toString())
            txtAdd_new_team_ed.isGone=true
            showLoader()
        } else {
            id = storeData.getString(Constants.COACH_ID)
            token = storeData.getString(Constants.COACH_TOKEN)
            val event_id = intent.getStringExtra("EVENT_ID")
            Log.e("TAG", "onCreate:event id is ===$event_id ")
            txtAdd_new_team_ed.isVisible=true

            controller = TeamListController(this, this)
            controller.callTeamLostApi(id, token, event_id.toString())
            deleteTeamCOntroller = DeleteTeamCOntroller(this, this)
        showLoader()

        }

//
        txtTitle.text = "TEAM LIST"
        imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }


        txtAdd_new_team_ed.setOnClickListener {
            val event_id = intent.getStringExtra("EVENT_ID")

            Log.e("TAG", "onCreate: ========$event_id")
            val intent = Intent(this, AddTeamsFinalActivity::class.java)
            intent.putExtra("EVENT_ID", event_id)
            intent.putExtra("EVENT_DETAIL", "event_detail")
            startActivity(intent)
            finish()
        }


    }

//    private fun setTopBar() {
//        imgBack.visibility= View.VISIBLE
//        imgBack.setOnClickListener { finish() }
//        txtTitle.text=getString(R.string.teams1)
//    }

    override fun onTeamListSuccess(response: List<TeamListResult?>?) {
        hideLoader()

        var TeamAdapger = TeamListEDAdapter(this, response!!)
        rv_team_list_ed.adapter = TeamAdapger
        TeamAdapger.iDeleteClickListner = this
        TeamAdapger.iItemClickListner = this
        TeamAdapger.notifyDataSetChanged()

    }

    override fun deleteTeamSuccessful() {

        hideLoader()
//        var storeData = StoreUserData(this)
//        val id = storeData.getString(Constants.COACH_ID)
//        val token = storeData.getString(Constants.COACH_TOKEN)
//        val event_id = intent.getStringExtra("EVENT_ID")
//
//        controller = TeamListController(this, this)
//        controller.callTeamLostApi(id, token, event_id.toString())

        showToast("DELETE TEAM SUCCESSFUL")


//        var storeData = StoreUserData(this)
//        val id = storeData.getString(Constants.COACH_ID)
//        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")
//        Log.e("TAG", "onCreate:event id is ===$event_id ")
        controller.callTeamLostApi(id, token, event_id.toString())
//        showLoader()

    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)

    }

    override fun onDeleteClick(deleteTeam: TeamListResult) {
        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val teamid = deleteTeam.getTeamId().toString()
        showLoader()
        deleteTeamCOntroller.callDeleteTeamApi(id, token, teamid)

    }

    override fun onEditClick(editTeam: TeamListResult) {
    }

    override fun onItemClick(itemCLick: TeamListResult) {

        val event_id = intent.getStringExtra("EVENT_ID")
        val team_id = itemCLick.getTeamId().toString()
        val tam_name = itemCLick.getTeamName().toString()
        val team_description = itemCLick.getDescription().toString()
        val team_iage = itemCLick.getImage().toString()

        Log.e("TAG", "onCreate: ========$event_id")
        val intent = Intent(this, TeamDetailActivity::class.java)
        intent.putExtra("EVENT_ID", event_id)
        intent.putExtra("TEAM_ID", team_id)
        intent.putExtra("TEAM_NAME", tam_name)
        intent.putExtra("TEAM_DESCRIPTION", team_description)
        intent.putExtra("TEAM_Image", team_iage)

        intent.putExtra("EVENT_DETAIL", "event_detail")
        startActivity(intent)
        finish()


    }
}