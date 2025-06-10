package com.xtrane.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.xtrane.R
import com.xtrane.adapter.CoachTeamListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityCoachesDetailBinding
import com.xtrane.retrofit.coachteamdata.CoachTeamResult
import com.xtrane.retrofit.controller.CoachTeamListController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.ICoachTeamListController
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.viewinterface.ICoachTeamListView

class CoachesDetailActivity : BaseActivity(), CoachTeamListAdapter.ITeamClickListner,
    ICoachTeamListView {
    private lateinit var binding: ActivityCoachesDetailBinding

    lateinit var coachTeamController: ICoachTeamListController
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_coaches_detail)
        binding = ActivityCoachesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        coachTeamController.callCoachTeamListApi(userId!!, token!! , id.toString())
        showLoader()

        binding.tvCoachName.text = name.toString()
        binding.tvCoachEmail.text = email.toString()
        binding.tvCoachContact.text = contact.toString()

        Glide.with(this)
            .load(image)
            .into(binding.ivCachImage)
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
        binding.topbar.imgBack.visibility = View.VISIBLE
        binding.topbar.imgBack.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.topbar.txtTitle.text = getString(R.string.coaches_detail)
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

            binding.rvCoachTeamList.isVisible=true
            binding.txtTeamsNotFound.isGone=true

            val coachTeamAdapter = CoachTeamListAdapter(this, response!!)
            coachTeamAdapter.iTeamClickListner = this
            binding.rvCoachTeamList.adapter = coachTeamAdapter
            coachTeamAdapter.notifyDataSetChanged()

        }else{

            binding.rvCoachTeamList.isGone=true
            binding.txtTeamsNotFound.isVisible=true

        }

    }

    override fun onFail(message: String?, e: Exception?) {

        hideLoader()
        showToast(message)
        binding.rvCoachTeamList.isGone=true
        binding.txtTeamsNotFound.isVisible=true
    }
}