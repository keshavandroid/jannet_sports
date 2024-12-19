package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MatchListAdapter
import com.e.jannet_stable_code.adapter.TeamListAdapter
import com.e.jannet_stable_code.databinding.ActivityAddedMatchlistBinding
import com.e.jannet_stable_code.databinding.ActivityMatchListBinding
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteMacthView
import com.e.jannet_stable_code.viewinterface.IMatchListView

class AddedMatchlistActivity : BaseActivity(), IMatchListView,
    MatchListAdapter.IDeleteMatchClickListner, MatchListAdapter.IEditMatchClickListner,
    IDeleteMacthView {
    lateinit var controller: IMatchListController
    lateinit var deleteMatchController: IDeleteMatchController
    private lateinit var binding: ActivityAddedMatchlistBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_added_matchlist)
        binding = ActivityAddedMatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val eventid = intent.getStringExtra("EVENT_ID")
        controller = MatchListController(this, this)
        deleteMatchController = DeleteMatchCOntroller(this, this)
        controller.callMatchListApi(id, token, eventid.toString())
        showLoader()

        binding.topbarAddedMatch.txtTitle.text = "MATCH LIST"
        binding.topbarAddedMatch.imgBack.setOnClickListener {

            onBackPressed()
        }

        binding.txtAddNewMatch.setOnClickListener {

            val intent = Intent(this, AddMatchFActivity::class.java)
            intent.putExtra("EVENT_ID", eventid)
            startActivity(intent)
            finish()
        }

        binding.txtDoneMatch.setOnClickListener {

            val event_id = intent.getStringExtra("EVENT_ID")
            val intent = Intent(
                this, FinalAddEventActivity
                ::class.java
            )
            intent.putExtra("EVENT_ID", event_id)
            startActivity(intent)
            finish()
        }
    }

    override fun onMatchListSuccess(response: ArrayList<MatchListResult?>?) {

        var MatchAdapger = MatchListAdapter(this, response)
        binding.rvMatchList.adapter = MatchAdapger
        MatchAdapger.iEditClickListner = this
        MatchAdapger.iDeleteClickListner = this
        MatchAdapger.notifyDataSetChanged()

        hideLoader()
        showToast("MatchList Successful ")
    }

    override fun onDeleteMatchSuccessfull() {

        showToast("Delete Match Successful")

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val eventid = intent.getStringExtra("EVENT_ID")
        controller = MatchListController(this, this)
        controller.callMatchListApi(id, token, eventid.toString())
        showLoader()
    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)

    }

    override fun deletematch(deleteMatch: MatchListResult) {

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val matchid = deleteMatch.getMatchId().toString()

        deleteMatchController.callDeleteMatchApi(id, token, matchid.toString())


    }

    override fun editmatch(editMatch: MatchListResult) {

        val event_id = intent.getStringExtra("EVENT_ID")

        val matchid = editMatch.getMatchId().toString()
        val  team_a_name = editMatch.getTeamAName().toString()
        val team_b_name = editMatch.getTeamBName().toString()
        val team_a_id = editMatch.getTeamAId().toString()
                val team_b_id = editMatch.getTeamBId().toString()
        val team_a_image = editMatch.getTeamAImage().toString()
        val team_b_image = editMatch.getTeamBImage().toString()
        val time = editMatch.getTime().toString()
        val coat = editMatch.getCoat().toString()

        val intent = Intent(
            this, EditMatchActivity
            ::class.java
        )
        intent.putExtra("MATCH_ID", matchid)
        intent.putExtra("EVENT_ID", event_id)
        intent.putExtra("TEAM_A_NAME", team_a_name)
        intent.putExtra("TEAM_B_NAME", team_b_name)
        intent.putExtra("TEAM_A_IMAGE", team_a_image)
        intent.putExtra("TEAM_B_IMAE", team_b_image)
        intent.putExtra("TEAM_A_ID",team_a_id, )
        intent.putExtra("TEAM_B_ID",team_b_id )

        intent.putExtra("TIME", time)
        intent.putExtra("COAT", coat)


        startActivity(intent)
        finish()
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

}