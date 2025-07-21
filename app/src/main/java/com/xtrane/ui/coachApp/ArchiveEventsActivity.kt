package com.xtrane.ui.coachApp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xtrane.adapter.EventListAdapter
import com.xtrane.adapter.TeamListEDAdapter
import com.xtrane.databinding.ActivityArchiveEventsBinding
import com.xtrane.databinding.ActivityTeamsBinding
import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.controller.*
import com.xtrane.retrofit.teamlistdata.TeamListResult
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.coachApp.addEventScreen.AddTeamsFinalActivity
import com.xtrane.ui.coachApp.addEventScreen.TeamDetailActivity
import com.xtrane.ui.parentsApp.EventDetailsActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.ICoachEventListView
import com.xtrane.viewinterface.IDeleteTeamView
import com.xtrane.viewinterface.ITeamListView

class ArchiveEventsActivity : BaseActivity(), ICoachEventListView {

    var id = ""
    var token = ""

    override fun getController(): IBaseController? {
        return null
    }
    lateinit var controller: ICoachEventListController

    private lateinit var binding : ActivityArchiveEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_teams)
        binding = ActivityArchiveEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setTopBar()


        val storedata = StoreUserData(this@ArchiveEventsActivity)
        id = storedata.getString(Constants.COACH_ID)
        token = storedata.getString(Constants.COACH_TOKEN)
        val user_type = storedata.getString(Constants.COACH_TYPE)

//        if (storedata.getString(Constants.COACH_ID)
//                .trim() == null || storedata.getString(Constants.COACH_ID).trim().isEmpty()
//        )
//        {
//
//
//            id = storedata.getString(Constants.COACH_ID)
//            token = storedata.getString(Constants.COACH_TOKEN)
//            val user_type = storedata.getString(Constants.COACH_TYPE)
//
//        }

//
        binding.topBar.txtTitle.text = "Archive Events"
        binding.topBar.imgBack.setOnClickListener {

            onBackPressed()
            finish()
        }
        showLoader()
        controller = CoachArchiveEventListController(this@ArchiveEventsActivity, this)
        controller.callCoachEventListApi(id, token, "", "")

    }

    override fun onCoachEventListSuccess(response: List<CoachEventListResult?>?) {
        if (response != null) {
            hideLoader()
            setListAdapter(response!!.reversed())

        }
    }
    private fun setListAdapter(result: List<CoachEventListResult?>?) {
        binding.rcvEventList.adapter = EventListAdapter(
            result, this@ArchiveEventsActivity,
            object : EventListAdapter.AdapterListInterface {
                override fun onItemSelected(position: Int, data: CoachEventListResult) {
                    startActivity(
                        Intent(this@ArchiveEventsActivity, EventDetailsActivity::class.java).putExtra(
                            "from",
                            "coachPersonal"
                        ).putExtra("eventId", data.getId().toString())
                    )
                    Log.e("CHome", "==event id====${data.getId().toString()}")

                }

            })
    }


    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)

    }


}