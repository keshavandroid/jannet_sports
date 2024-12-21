package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.TeamListAdapter
import com.e.jannet_stable_code.databinding.ActivityAddTeamsBinding
import com.e.jannet_stable_code.databinding.ActivityAddedMatchlistBinding
import com.e.jannet_stable_code.databinding.ActivityAddedTeamListBinding
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteTeamView
import com.e.jannet_stable_code.viewinterface.ITeamListView


class AddedTeamListActivity : BaseActivity(), ITeamListView, TeamListAdapter.IEditTeamClickListner,
    TeamListAdapter.IDeleteClickListner, IDeleteTeamView {

    override fun getController(): IBaseController? {
        return null
    }

    lateinit var deleteTeamCOntroller: IDeleteTeamController
    lateinit var controller: ITeamListController
    private lateinit var bind: ActivityAddedTeamListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  setContentView(R.layout.activity_added_team_list)
        bind = ActivityAddedTeamListBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")
        Log.e("TAG", "onCreate:event id is ===$event_id ")
        controller = TeamListController(this, this)
        controller.callTeamLostApi(id, token, event_id.toString())
        showLoader()

        bind.topbarAddedTeams.txtTitle.text = "TEAM LIST"
        bind.topbarAddedTeams.imgBack.setOnClickListener {

            onBackPressed()

        }

        bind.txtAddNewTeam.setOnClickListener {
            val event_id = intent.getStringExtra("EVENT_ID")

            Log.e("TAG", "onCreate: ========$event_id")
            val intent = Intent(this, AddTeamsFinalActivity::class.java)
            intent.putExtra("EVENT_ID", event_id)
            startActivity(intent)
            finish()
        }

        bind.txtDoneTeam.setOnClickListener {
            val intent = Intent(this, AddMatchActivity::class.java)
            intent.putExtra("EVENT_ID", event_id.toString())
            startActivity(intent)
        }

        deleteTeamCOntroller = DeleteTeamCOntroller(this, this)
    }

    override fun onTeamListSuccess(response: List<TeamListResult?>?) {

        var TeamAdapger = TeamListAdapter(this, response!!)
        bind.rvTeamList.adapter = TeamAdapger
        TeamAdapger.iEditClickListner = this
        TeamAdapger.iDeleteClickListner = this
        TeamAdapger.notifyDataSetChanged()

        hideLoader()

    }

    override fun deleteTeamSuccessful() {

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")


        hideLoader()
        showToast("Delete Team Successful")
        showLoader()
        controller.callTeamLostApi(id, token, event_id.toString())
    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
        onBackPressed()
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

        val teamID = editTeam.getTeamId().toString()
        val event_id = intent.getStringExtra("EVENT_ID")
        val team_desriptiom = editTeam.getDescription().toString()
        val team_name = editTeam.getTeamName().toString()

        val team_image = editTeam.getImage().toString()



        val intent = Intent(this, EditTeamActivity::class.java)
        intent.putExtra("TEAM_ID", teamID.toString())
        intent.putExtra("EVENT_ID",event_id.toString())
        intent.putExtra("TEAM_DETAIL","Added_Team")
        intent.putExtra("TEAM_DESCRIPTION",team_desriptiom)
        intent.putExtra("TEAM_NAME",team_name)
        intent.putExtra("TEAM_Image",team_image)

        startActivity(intent)

    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Home Screen")
        //set message for alert dialog
        builder.setMessage("Are you Sure you Want to Go Home Screen?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            Toast.makeText(applicationContext,"clicked yes", Toast.LENGTH_LONG).show()

            super.onBackPressed()


        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(applicationContext,"clicked cancel\n operation cancel", Toast.LENGTH_LONG).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(applicationContext,"clicked No", Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onResume() {
        super.onResume()

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")
        Log.e("TAG", "onCreate:event id is ===$event_id ")
        controller = TeamListController(this, this)
        controller.callTeamLostApi(id, token, event_id.toString())
        showLoader()

    }

}