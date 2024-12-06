package com.e.jannet_stable_code.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.CoachTeamListAdapter
import com.e.jannet_stable_code.retrofit.coachteamdata.CoachTeamResult
import com.e.jannet_stable_code.retrofit.controller.CoachTeamListController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.ICoachTeamListController
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.viewinterface.ICoachTeamListView
import kotlinx.android.synthetic.main.activity_coaches_detail.*
import kotlinx.android.synthetic.main.topbar_layout.*

class CoachesDetailActivity : BaseActivity(), CoachTeamListAdapter.ITeamClickListner,
    ICoachTeamListView {

    lateinit var coachTeamController: ICoachTeamListController
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coaches_detail)

        setTopBar()

        val userId = SharedPrefUserData(this).getSavedData().id
        val token = SharedPrefUserData(this).getSavedData().token
        coachTeamController = CoachTeamListController(this, this)

        val name = intent.getStringExtra("NAME")
        val email = intent.getStringExtra("EMAIL")
        val contact = intent.getStringExtra("CONTACT")
        val birth = intent.getStringExtra("BIRTH")
        val gender = intent.getStringExtra("GENDER")
        val id = intent.getStringExtra("CoachID")
        val image = intent.getStringExtra("IMAGE")

        coachTeamController.callCoachTeamListApi(userId, token, id.toString())
        showLoader()

        tv_coach_name.text = name.toString()
        tv_coach_email.text = email.toString()
        tv_coach_contact.text = contact.toString()

        Glide.with(this)
            .load(image)
            .into(iv_cach_image)
    }

    private fun openTeamDetails() {
        val id = intent.getStringExtra("CoachID")

//        startActivity(Intent(this,TeamDetailsActivity::class.java).putExtra("from","coachesdetail"))
        val intent = Intent(this, TeamDetailsActivity::class.java)
        intent.putExtra("CoachID", id.toString())
        intent.putExtra("COACHDETAIL", "coachdetail")
        startActivity(intent)
    }

    private fun setTopBar() {
        imgBack.visibility = View.VISIBLE
        imgBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        txtTitle.text = getString(R.string.coaches_detail)
    }

    override fun onTeamClick(response: CoachTeamResult?) {

        val id = response?.getTeamId()
        val intent = Intent(this, TeamDetailsActivity::class.java)
        intent.putExtra("CoachID", id.toString())
        intent.putExtra("COACHDETAIL", "coachdetail")
        intent.putExtra("team_id", response?.getTeamId().toString())
        intent.putExtra("TEAMNAME", response?.getTeamName())
        intent.putExtra("COACHNAME", response?.getCoachName())
        intent.putExtra("SPORTSTYPE", response?.getSportsType())
        intent.putExtra("DESCRIPTION", response?.getDescription())
        intent.putExtra("FEES", response?.getFees().toString())
        intent.putExtra("isJoin", response?.getIsJoin().toString())
        intent.putExtra("IMAGE", response?.getImage().toString())



        startActivity(intent)


    }

    override fun onCoachTeamListSuccess(response: List<CoachTeamResult?>?) {

        hideLoader()

        if (response!=null){

            rv_coach_team_list.isVisible=true
            txt_teams_not_found.isGone=true

            val coachTeamAdapter = CoachTeamListAdapter(this, response!!)
            coachTeamAdapter.iTeamClickListner = this
            rv_coach_team_list.adapter = coachTeamAdapter
            coachTeamAdapter.notifyDataSetChanged()

        }else{

            rv_coach_team_list.isGone=true
            txt_teams_not_found.isVisible=true

        }

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
        rv_coach_team_list.isGone=true
        txt_teams_not_found.isVisible=true
    }
}