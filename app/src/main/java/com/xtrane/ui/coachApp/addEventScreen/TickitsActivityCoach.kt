package com.xtrane.ui.coachApp.addEventScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xtrane.R
import com.xtrane.adapter.NonParticipentSelectionListAdapter
import com.xtrane.adapter.TicketsAdapter
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.databinding.ActivityTickitsCoachBinding
import com.xtrane.retrofit.controller.GetTicketController
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IGetTicketsCOntroller
import com.xtrane.retrofit.gettickitsdata.GetTicketsResullt
import com.xtrane.ui.BaseActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IGetTicketsView

class TickitsActivityCoach : BaseActivity(),IGetTicketsView {

    lateinit var controller:IGetTicketsCOntroller
    override fun getController(): IBaseController? {
        return null
    }
    private lateinit var bind: ActivityTickitsCoachBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_tickits_coach)
        bind = ActivityTickitsCoachBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.topbar.txtTitle.text = "TICKETS"
        bind.topbar.imgEdit.isGone=true
        bind.topbar.imgBack.visibility= View.VISIBLE
        bind.topbar.imgBack.setOnClickListener { finish() }

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)


        val eventid = intent.getStringExtra("EVENT_ID")

        controller=GetTicketController(this,this)
        controller.callGetTicketsApi(id,token,eventid.toString())
        showLoader()



    }

    override fun onGetTicktetsSuccess(response: List<GetTicketsResullt?>?) {

        hideLoader()

        var TicketsAdapter = TicketsAdapter(this, response!!)
        bind.rvTicketList.adapter = TicketsAdapter
        TicketsAdapter.notifyDataSetChanged()

    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)


    }
}