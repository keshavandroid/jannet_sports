package com.xtrane.ui.parentsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xtrane.R
import com.xtrane.adapter.MatchTabListAdapter
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityMatchHistoryBinding
import com.xtrane.databinding.ActivityMatchListBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.GetParentMatchController
import com.xtrane.retrofit.matchlistdata.MatchListResult
import com.xtrane.utils.Utilities


class MatchHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_match_history)
        binding = ActivityMatchHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }

    private fun initView() {
        binding.topbarMatchHistory.txtTitle.text = "MATCH HISTORY"
        binding.topbarMatchHistory.imgBack.setOnClickListener { finish() }

        GetParentMatchController(this, object : ControllerInterface,
            MatchTabListAdapter.IMatchItemClickClickListner {
            override fun onFail(error: String?) {

            }
            override fun <T> onSuccess(response: T, method: String) {
                try {

                    val resp = response as  List<MatchListResult?>?
                    var adapter = MatchTabListAdapter(this@MatchHistoryActivity, resp)
                    binding.rvMatchHistory.adapter = adapter
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