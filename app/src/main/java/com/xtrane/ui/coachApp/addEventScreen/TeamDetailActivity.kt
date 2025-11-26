package com.xtrane.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.databinding.ActivityTeamDetailBinding
import com.xtrane.retrofit.controller.*
import com.xtrane.retrofit.teamdetaildata.TeamDetailResult
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.parentsApp.TeamsActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IDeleteTeamView
import com.xtrane.viewinterface.TeamDetailView


class TeamDetailActivity : BaseActivity(), TeamDetailView, IDeleteTeamView {
    lateinit var controller: ITeamDetailController
    lateinit var deleteTeamController: IDeleteTeamController
    var id = ""
    var token = ""
    override fun getController(): IBaseController? {
        return null
    }

    private lateinit var bind: ActivityTeamDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_team_detail)
        bind = ActivityTeamDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var storeData = StoreUserData(this)
        id = storeData.getString(Constants.COACH_ID)
        token = storeData.getString(Constants.COACH_TOKEN)

        val event_id = intent.getStringExtra("EVENT_ID")
        val teamId = intent.getStringExtra("TEAM_ID")
        val team_desriptiom = intent.getStringExtra("TEAM_DESCRIPTION")
        val team_name = intent.getStringExtra("TEAM_NAME")
        val team_image = intent.getStringExtra("TEAM_Image")
        val coach_id = intent.getStringExtra("coach_id")
        val EventType = intent.getStringExtra("EventType")


        if (EventType.equals("Draft",ignoreCase = true))
        {
            bind.tvParticipent.visibility= View.GONE
        }
        else
        {
            bind.tvParticipent.visibility= View.VISIBLE

        }

        bind.teamDetail.txtTitle.text = "Team Detail"
        bind.teamDetail.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }

        if (team_name != null) {


        }

        controller = TeamDetailController(this, this)

        if (storeData.getString(Constants.COACH_ID)
                .trim() == null || storeData.getString(Constants.COACH_ID).trim().isEmpty()
        ) {
            bind.ivEditTeamEventDetail.isGone = true
            bind.ivDeleteTeamEventDetail.isGone = true
            bind.tvParticipent.isGone = true

            val data = SharedPrefUserData(this).getSavedData()

            id = data.id.toString()
            token = data.token.toString()
            controller.callEventDetailApi(id, token, teamId.toString(), event_id.toString())
            showLoader()
        } else {

            controller.callEventDetailApi(id, token, teamId.toString(), event_id.toString())
            showLoader()
        }
        deleteTeamController = DeleteTeamCOntroller(this, this)

        bind.tvParticipent.setOnClickListener {
            val intent = Intent(this, AddMemberInTeamActivity::class.java)
            intent.putExtra("EVENT_ID", event_id.toString())
            intent.putExtra("TEAM_ID", teamId.toString())
            intent.putExtra("coach_id", coach_id.toString())
            startActivity(intent)


        }


        bind.ivEditTeamEventDetail.setOnClickListener {

            val event_id = intent.getStringExtra("EVENT_ID")
            val teamId = intent.getStringExtra("TEAM_ID")
            val team_desriptiom = intent.getStringExtra("TEAM_DESCRIPTION")
            val team_name = intent.getStringExtra("TEAM_NAME")
            val team_image = intent.getStringExtra("TEAM_Image")

            Log.e("TAG", "onCreate: team image is ====$team_image")

            val intent = Intent(this, EditTeamActivity::class.java)
            intent.putExtra("EVENT_ID", event_id.toString())
            intent.putExtra("TEAM_ID", teamId.toString())
            intent.putExtra("TEAM_DETAIL", "team_detail")
            intent.putExtra("TEAM_DESCRIPTION", team_desriptiom)
            intent.putExtra("TEAM_NAME", team_name)
            intent.putExtra("TEAM_Image", team_image)
            startActivity(intent)


        }

        bind.ivDeleteTeamEventDetail.setOnClickListener {

            showLoader()
            var storeData = StoreUserData(this)
            val id = storeData.getString(Constants.COACH_ID)
            val token = storeData.getString(Constants.COACH_TOKEN)
            val event_id = intent.getStringExtra("EVENT_ID")
            val teamId = intent.getStringExtra("TEAM_ID")


            deleteTeamController.callDeleteTeamApi(id, token, teamId.toString())


        }


    }

    override fun onTeamDetailSuccess(response: List<TeamDetailResult?>?) {

        hideLoader()
        bind.tvTeamNameEventDetail.text ="Team Name : " + response!![0]!!.getTeamName().toString()
        bind.tvCreated.text ="Created By : " +response[0]!!.getCoachName().toString()
        bind.tvSportsTypeTeamDetail.text = response!![0]!!.getSportsName().toString()
        bind.tvParticipentTeamDetail.text = response!![0]!!.getParticipants().toString()
        bind.tvTeamDetailDescription.text = response!![0]!!.getDescription().toString()

        Glide.with(this)
            .load(response!![0]!!.getImage())
            .into(bind.ivTeamImageEd)

    }

    override fun deleteTeamSuccessful() {
        hideLoader()
        val event_id = intent.getStringExtra("EVENT_ID")

        hideLoader()
        showToast("DELETE TEAM SUCCESSFUL")
        val intent = Intent(this, TeamsActivity::class.java)
        intent.putExtra("EVENT_ID", event_id)
        startActivity(intent)
        finish()

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)

    }


}