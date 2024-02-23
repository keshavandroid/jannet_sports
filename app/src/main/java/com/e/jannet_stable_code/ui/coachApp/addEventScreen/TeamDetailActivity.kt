package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.teamdetaildata.TeamDetailResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.parentsApp.TeamsActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IDeleteTeamView
import com.e.jannet_stable_code.viewinterface.TeamDetailView
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.android.synthetic.main.topbar_layout.*

class TeamDetailActivity : BaseActivity(), TeamDetailView, IDeleteTeamView {
    lateinit var controller: ITeamDetailController
    lateinit var deleteTeamController: IDeleteTeamController
    var id = ""
    var token = ""
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        var storeData = StoreUserData(this)
        id = storeData.getString(Constants.COACH_ID)
        token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")
        val teamId = intent.getStringExtra("TEAM_ID")
        val team_desriptiom = intent.getStringExtra("TEAM_DESCRIPTION")
        val team_name = intent.getStringExtra("TEAM_NAME")
        val team_image = intent.getStringExtra("TEAM_Image")


        txtTitle.text = "TEAM Detail"
        imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }

        if (team_name != null) {


        }

        controller = TeamDetailController(this, this)

        if (storeData.getString(Constants.COACH_ID)
                .trim() == null || storeData.getString(Constants.COACH_ID).trim().isEmpty()
        ) {
            iv_edit_team_event_detail.isGone = true
            iv_delete_team_event_detail.isGone = true
            tv_participent.isGone = true

            val data = SharedPrefUserData(this).getSavedData()

            id = data.id
            token = data.token
            controller.callEventDetailApi(id, token, teamId.toString(), event_id.toString())
            showLoader()
        } else {

            controller.callEventDetailApi(id, token, teamId.toString(), event_id.toString())
            showLoader()
        }
        deleteTeamController = DeleteTeamCOntroller(this, this)

        tv_participent.setOnClickListener {
            val intent = Intent(this, AddMemberInTeamActivity::class.java)
//            intent.putExtra("EVENT_ID", eventid.toString())
//            intent.putExtra("TEAM_ID", team_id.toString())
            startActivity(intent)


        }


        iv_edit_team_event_detail.setOnClickListener {

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

        iv_delete_team_event_detail.setOnClickListener {

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
        tv_team_name_event_detail.text = response!![0]!!.getTeamName().toString()
        tv_sports_type_team_Detail.text = response!![0]!!.getSportsName().toString()
        tv_participent_team_detail.text = response!![0]!!.getParticipants().toString()
        tv_team_detail_description.text = response!![0]!!.getDescription().toString()

        Glide.with(this)
            .load(response!![0]!!.getImage())
            .into(iv_team_image_ed)

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