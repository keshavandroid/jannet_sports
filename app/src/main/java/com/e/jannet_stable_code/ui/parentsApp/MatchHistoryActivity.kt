package com.e.jannet_stable_code.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.MatchTabListAdapter
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.GetParentMatchController
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult
import com.e.jannet_stable_code.utils.Utilities
import kotlinx.android.synthetic.main.activity_match_history.*
import kotlinx.android.synthetic.main.topbar_layout.*

class MatchHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_history)
        initView()
    }

    private fun initView() {
        txtTitle.text = "MATCH HISTORY"
        imgBack.setOnClickListener { finish() }

        GetParentMatchController(this, object : ControllerInterface,
            MatchTabListAdapter.IMatchItemClickClickListner {
            override fun onFail(error: String?) {

            }
            override fun <T> onSuccess(response: T, method: String) {
                try {

                    val resp = response as  List<MatchListResult?>?
                    var adapter = MatchTabListAdapter(this@MatchHistoryActivity, resp)
                    rv_match_history.adapter = adapter
                    adapter.iMatchItemClickClickListner = this
                    adapter.notifyDataSetChanged()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClick(result: MatchListResult) {
                //  var list:List<MatchListResult?>?=listOf<MatchListResult>()
                val intent = Intent (this@MatchHistoryActivity, RegisteredMatchActivity::class.java)
                intent.putExtra("getTeamAName",result.getTeamAName())
                intent.putExtra("getTeamAImage",result.getTeamAImage())
                intent.putExtra("getTeamBName",result.getTeamBName())
                intent.putExtra("getTeamBImage",result.getTeamBImage())
                intent.putExtra("date", Utilities.convertDateFormat(result.getDate().toString())+" "+result.getTime())
                startActivity(intent)


            }
        }).callApi()

    }
}