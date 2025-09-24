package com.xtrane.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xtrane.R
import com.xtrane.adapter.TeamListAdapter
import com.xtrane.adapter.TeamListEDAdapter
import com.xtrane.databinding.ActivityTeamsBinding
import com.xtrane.databinding.ActivityVenueBinding
import com.xtrane.retrofit.controller.*
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.retrofit.teamlistdata.TeamListResult
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.coachApp.addEventScreen.AddTeamsFinalActivity
import com.xtrane.ui.coachApp.addEventScreen.TeamDetailActivity
import com.xtrane.ui.coachApp.addEventScreen.TeamsDetailAndAddParticipentActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IDeleteTeamView
import com.xtrane.viewinterface.ITeamListView

class TeamsActivity : BaseActivity(), ITeamListView, TeamListEDAdapter.IEditTeamClickListner,
    TeamListEDAdapter.IDeleteClickListner, IDeleteTeamView, TeamListEDAdapter.IItemClickListner {

    var id = ""
    var token = ""

    override fun getController(): IBaseController? {
        return null
    }

    lateinit var deleteTeamCOntroller: IDeleteTeamController
    lateinit var controller: ITeamListController
    lateinit var EventDetailedResponse: EventDetailResponse

    private lateinit var binding: ActivityTeamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_teams)
        binding = ActivityTeamsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setTopBar()


        var storeData = StoreUserData(this)


        if (storeData.getString(Constants.COACH_ID).trim().isEmpty())
        {

            id = SharedPrefUserData(this).getSavedData().id!!
            token = SharedPrefUserData(this).getSavedData().token!!
            val event_id = intent.getStringExtra("EVENT_ID")
            controller = TeamListController(this, this)
            controller.callTeamLostApi(id, token, event_id.toString())
            binding.txtAddNewTeamEd.visibility=View.GONE
            showLoader()
        }
        else {
            id = storeData.getString(Constants.COACH_ID)
            token = storeData.getString(Constants.COACH_TOKEN)
            val event_id = intent.getStringExtra("EVENT_ID")
            Log.e("TAG", "onCreate:event id is ===$event_id ")
//            if (id.equals())
            binding.txtAddNewTeamEd.visibility=View.VISIBLE

            controller = TeamListController(this, this)
            controller.callTeamLostApi(id, token, event_id.toString())
            deleteTeamCOntroller = DeleteTeamCOntroller(this, this)
            showLoader()

        }
        if (intent.hasExtra("eventdetailresponse"))
        {
            EventDetailedResponse= intent.getSerializableExtra("eventdetailresponse") as EventDetailResponse

            Log.e("TAG", "EventDetailedResponse ==="+EventDetailedResponse.getResult()!!.get(0)!!.getEventType().toString())

            if (EventDetailedResponse.getResult()!!.get(0)!!.getEventType().equals("draft",ignoreCase = true))
            {
                binding.txtAddNewTeamEd.visibility=View.VISIBLE
            }
            else
            {
                if (EventDetailedResponse.getResult()!!.get(0)!!.getCoachID().equals(id))
                {
                    binding.txtAddNewTeamEd.visibility=View.VISIBLE
                }
                else
                {
                    binding.txtAddNewTeamEd.visibility=View.GONE
                }
            }


        }
//
        binding.teamListEd.txtTitle.text = "TEAM LIST"
        binding.teamListEd.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }


        binding.txtAddNewTeamEd.setOnClickListener {
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
        binding.rvTeamListEd.adapter = TeamAdapger
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