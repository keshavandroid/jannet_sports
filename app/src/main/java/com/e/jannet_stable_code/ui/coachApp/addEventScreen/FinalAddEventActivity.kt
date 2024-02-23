package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MatchListAdapter
import com.e.jannet_stable_code.adapter.MatchPriceAdapter
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.controller.IMatchListController
import com.e.jannet_stable_code.retrofit.controller.MatchListController
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.ui.coachApp.CoachMainActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.IMatchListView
import kotlinx.android.synthetic.main.activity_added_matchlist.*
import kotlinx.android.synthetic.main.activity_final_add_event.*
import kotlinx.android.synthetic.main.topbar_layout.*

class FinalAddEventActivity : BaseActivity(), IMatchListView {
    lateinit var controller: IMatchListController

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_add_event)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")

        controller = MatchListController(this, this)
        controller.callMatchListApi(id, token, event_id.toString())
        showLoader()


        txtTitle.text = "ADD MATCH PRICE"
        imgBack.setOnClickListener {

            val eventId = intent.getStringExtra("EVENT_ID")

            val intent = Intent(this, AddedMatchlistActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
            finish()
        }

        txt_finish_event.setOnClickListener {

            showLoader()
            txt_finish_event.postDelayed({

                val intent = Intent(this, CoachMainActivity::class.java)
                startActivity(intent)
                finish()

            }, 1000)


        }
    }

    override fun onMatchListSuccess(response: ArrayList<MatchListResult?>?) {
        hideLoader()

        var MatchAdapger = MatchPriceAdapter(this, response)
        rv_add_match_price_list.adapter = MatchAdapger
        MatchAdapger.notifyDataSetChanged()


    }

    override fun onFail(message: String?, e: Exception?) {
        hideLoader()
        showToast(message)

    }

    override fun onBackPressed() {

        val eventId = intent.getStringExtra("EVENT_ID")

        val intent = Intent(this, AddedMatchlistActivity::class.java)
        intent.putExtra("EVENT_ID", eventId)
        startActivity(intent)
        finish()

    }

//        val builder = AlertDialog.Builder(this)
//        //set title for alert dialog
//        builder.setTitle("Home Screen")
//        //set message for alert dialog
//        builder.setMessage("Are you Sure you Want to Go Home Screen?")
//        builder.setIcon(android.R.drawable.ic_dialog_alert)
//
//        //performing positive action
//        builder.setPositiveButton("Yes"){dialogInterface, which ->
//            Toast.makeText(applicationContext,"clicked yes", Toast.LENGTH_LONG).show()
//
//            super.onBackPressed()
//
//
//        }
//        //performing cancel action
//        builder.setNeutralButton("Cancel"){dialogInterface , which ->
//            Toast.makeText(applicationContext,"clicked cancel\n operation cancel", Toast.LENGTH_LONG).show()
//        }
//        //performing negative action
//        builder.setNegativeButton("No"){dialogInterface, which ->
//            Toast.makeText(applicationContext,"clicked No", Toast.LENGTH_LONG).show()
//        }
//        // Create the AlertDialog
//        val alertDialog: AlertDialog = builder.create()
//        // Set other dialog properties
//        alertDialog.setCancelable(false)
//        alertDialog.show()
//
//    }

}