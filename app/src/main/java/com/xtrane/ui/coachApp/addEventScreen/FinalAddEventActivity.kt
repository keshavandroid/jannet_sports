package com.xtrane.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.xtrane.R
import com.xtrane.adapter.MatchListAdapter
import com.xtrane.adapter.MatchPriceAdapter
import com.xtrane.databinding.ActivityEditMatchBinding
import com.xtrane.databinding.ActivityFinalAddEventBinding
import com.xtrane.retrofit.controller.IBaseController
import com.xtrane.retrofit.controller.IMatchListController
import com.xtrane.retrofit.controller.MatchListController
import com.xtrane.retrofit.matchlistdata.MatchListResult
import com.xtrane.ui.BaseActivity
import com.xtrane.ui.coachApp.CoachMainActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.viewinterface.IMatchListView


class FinalAddEventActivity : BaseActivity(), IMatchListView {
    lateinit var controller: IMatchListController
    private lateinit var bind: ActivityFinalAddEventBinding

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_final_add_event)
        bind = ActivityFinalAddEventBinding.inflate(layoutInflater)
        setContentView(bind.root)

        var storeData = StoreUserData(this)
        val id = storeData.getString(Constants.COACH_ID)
        val token = storeData.getString(Constants.COACH_TOKEN)
        val event_id = intent.getStringExtra("EVENT_ID")

        controller = MatchListController(this, this)
        controller.callMatchListApi(id, token, event_id.toString())
        showLoader()


        bind.topbarAddedMatch.txtTitle.text = "ADD MATCH PRICE"
        bind.topbarAddedMatch.imgBack.setOnClickListener {

            val eventId = intent.getStringExtra("EVENT_ID")

            val intent = Intent(this, AddedMatchlistActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
            finish()
        }

        bind.txtFinishEvent.setOnClickListener {

            showLoader()
            bind.txtFinishEvent.postDelayed({

                val intent = Intent(this, CoachMainActivity::class.java)
                startActivity(intent)
                finish()

            }, 1000)


        }
    }

    override fun onMatchListSuccess(response: ArrayList<MatchListResult?>?) {
        hideLoader()

        var MatchAdapger = MatchPriceAdapter(this, response)
        bind.rvAddMatchPriceList.adapter = MatchAdapger
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