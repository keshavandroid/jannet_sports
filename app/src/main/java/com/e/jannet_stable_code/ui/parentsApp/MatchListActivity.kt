package com.e.jannet_stable_code.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MatchListAdapter
import com.e.jannet_stable_code.adapter.MatchListAdapterED
import com.e.jannet_stable_code.adapter.ParticipantsMatchListAdapter
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityMatchListBinding
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.AddMatchActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.AddMatchFActivity
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.EditMatchActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteMacthView
import com.e.jannet_stable_code.viewinterface.IMatchListView


class MatchListActivity : BaseActivity(), IMatchListView, MatchListAdapterED.IEditMatchClickListner,
    MatchListAdapterED.IDeleteMatchClickListner,
    IDeleteMacthView {

    lateinit var controller: IMatchListController
    lateinit var deleteMatchController: IDeleteMatchController
    var id = ""
    var token = ""
    private lateinit var binding: ActivityMatchListBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_match_list)
        binding = ActivityMatchListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var storeData = StoreUserData(this)
        val eventID = intent.getStringExtra("EVENT_ID")
        Log.e("TAG", "onCreate: matchlist event id is $eventID")
        controller = MatchListController(this, this)


        if (storeData.getString(Constants.COACH_ID)
                .trim() == null || storeData.getString(Constants.COACH_ID).trim().isEmpty()
        ) {

            id = SharedPrefUserData(this).getSavedData().id
            token = SharedPrefUserData(this).getSavedData().token
            controller.callMatchListApi(id, token, eventID.toString())

            binding.txtAddMatchEventDetail.isGone=true
            showLoader()
        } else {
            id = storeData.getString(Constants.COACH_ID)
            token = storeData.getString(Constants.COACH_TOKEN)
            controller.callMatchListApi(id, token, eventID.toString())
            binding.txtAddMatchEventDetail.isVisible=true

            showLoader()

        }
        deleteMatchController = DeleteMatchCOntroller(this, this)


        binding.topbarAddedMatch.txtTitle.text = "MATCH LIST"
        binding.topbarAddedMatch.imgBack.setOnClickListener {
           finish()
        }


//        if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
//                .equals("coachPersonal")
//        ){
//            txtAddMatch.visibility=View.VISIBLE
//        }else if (intent.getStringExtra("from") != null && intent.getStringExtra("from")
//                .equals("notCoach")
//        ){
//            txtAddMatch.visibility=View.GONE
//        }


        setTopBar()

        binding.txtAddMatchEventDetail.setOnClickListener {
//            startActivity(Intent(this,
//                AddMatchActivity::class.java))

            val eventID = intent.getStringExtra("EVENT_ID")


            var intent = Intent(this, AddMatchFActivity::class.java)
            intent.putExtra("EVENT_ID", eventID.toString())
            intent.putExtra("EVENT_DETAIL", "event")
            startActivity(intent)
            finish()

        }
    }

    private fun setTopBar() {
        binding.topbarAddedMatch.imgBack.visibility = View.VISIBLE
        binding.topbarAddedMatch.imgBack.setOnClickListener { finish() }
        binding.topbarAddedMatch.txtTitle.text = getString(R.string.match_list)
    }

    override fun onMatchListSuccess(response: ArrayList<MatchListResult?>?) {
        hideLoader()

        var storeData = StoreUserData(this)

        if (storeData.getString(Constants.COACH_ID)
                .trim() == null || storeData.getString(Constants.COACH_ID).trim().isEmpty()
        ) {

            var MatchAdapger = ParticipantsMatchListAdapter(this, response)
            binding.rvMatchListEd.adapter = MatchAdapger
            MatchAdapger.notifyDataSetChanged()

        }else {

            var MatchAdapger = MatchListAdapterED(this, response)
            binding.rvMatchListEd.adapter = MatchAdapger
            MatchAdapger.iEditClickListner = this
            MatchAdapger.iDeleteClickListner = this
            MatchAdapger.notifyDataSetChanged()
        }
        showToast("MatchList Successful ")
    }

    override fun onDeleteMatchSuccessfull() {
        hideLoader()

        showToast("Match Delete Successful")
        var storeData = StoreUserData(this)
        val eventID = intent.getStringExtra("EVENT_ID")
        Log.e("TAG", "onCreate: matchlist event id is $eventID")
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        controller = MatchListController(this, this)
        controller.callMatchListApi(id, token, eventID.toString())
        showLoader()
    }

    override fun onFail(message: String?, e: Exception?) {

        showToast(message)
        hideLoader()
    }

    override fun deletematch(deleteMatch: MatchListResult) {

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val matchid = deleteMatch.getMatchId().toString()

        deleteMatchController.callDeleteMatchApi(id, token, matchid.toString())
        showLoader()
    }

    override fun editmatch(editMatch: MatchListResult) {
        val event_id = intent.getStringExtra("EVENT_ID")
        val team_a_name = editMatch.getTeamAName().toString()
        val team_b_name = editMatch.getTeamBName().toString()
        val team_a_image = editMatch.getTeamAImage().toString()
        val team_b_image = editMatch.getTeamBImage().toString()

        val time = editMatch.getTime().toString()
        val coat = editMatch.getCoat().toString()
        val team_a_id = editMatch.getTeamAId().toString()
        val team_b_id = editMatch.getTeamBId().toString()

        val matchid = editMatch.getMatchId().toString()
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
        intent.putExtra("TIME", time)
        intent.putExtra("COAT", coat)
        intent.putExtra("EVENT_DETAIL", "event_detail")
        intent.putExtra("TEAM_A_ID", team_a_id)
        intent.putExtra("TEAM_B_ID", team_b_id)

        startActivity(intent)
        finish()
    }
}