package com.e.jannet_stable_code.ui.parentsApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityRegisteredMatchBinding

class RegisteredMatchActivity : AppCompatActivity() {
    var getTeamAName = ""
    var getTeamAImage = ""
    var getTeamBName = ""
    var getTeamBImage = ""
    var date = ""
    private lateinit var binding: ActivityRegisteredMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
            //setContentView(R.layout.activity_registered_match)
        binding = ActivityRegisteredMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var txt_match_date_ped = findViewById<TextView>(R.id.txt_match_date_ped)
        var iv_team_a_ped = findViewById<ImageView>(R.id.iv_team_a_ped)
        var tv_taem_a_Name_ped = findViewById<TextView>(R.id.tv_taem_a_Name_ped)
        var iv_team_b_ped = findViewById<ImageView>(R.id.iv_team_b_ped)
        var tv_team_b_name_ped = findViewById<TextView>(R.id.tv_team_b_name_ped)

        binding.topbarAddedMatch.txtTitle.text = "MATCH LIST"

        getTeamAName = intent.getStringExtra("getTeamAName").toString()
        getTeamAImage = intent.getStringExtra("getTeamAImage").toString()
        getTeamBName = intent.getStringExtra("getTeamBName").toString()
        getTeamBImage = intent.getStringExtra("getTeamBImage").toString()
        date = intent.getStringExtra("date").toString()


        txt_match_date_ped.text = date
        Glide.with(iv_team_a_ped.context)
            .load(getTeamAImage)
            .into(iv_team_a_ped)

        tv_taem_a_Name_ped.text = getTeamAName
        Glide.with(iv_team_b_ped.context)
            .load(getTeamBImage)
            .into(iv_team_b_ped)

        tv_team_b_name_ped.text = getTeamBName

        binding.topbarAddedMatch.imgBack.setOnClickListener { finish() }

    }
}