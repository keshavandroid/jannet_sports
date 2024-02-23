package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.NonParticipentSelectionListAdapter
import com.e.jannet_stable_code.adapter.TicketsAdapter
import com.e.jannet_stable_code.retrofit.controller.GetTicketController
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IGetTicketsCOntroller
import com.e.jannet_stable_code.retrofit.gettickitsdata.GetTicketsResullt
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IGetTicketsView
import kotlinx.android.synthetic.main.activity_add_participant.*
import kotlinx.android.synthetic.main.activity_tickits_coach.*
import kotlinx.android.synthetic.main.topbar_layout.*

class TickitsActivityCoach : BaseActivity(),IGetTicketsView {

    lateinit var controller:IGetTicketsCOntroller
    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickits_coach)

        txtTitle.text = "TICKETS"
        imgEdit.isGone=true
        imgBack.visibility= View.VISIBLE
        imgBack.setOnClickListener { finish() }

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
        rv_ticket_list.adapter = TicketsAdapter
        TicketsAdapter.notifyDataSetChanged()

    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)


    }
}