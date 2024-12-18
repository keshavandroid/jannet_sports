package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityCreateTeamBinding
import com.e.jannet_stable_code.databinding.ActivityTeamsBinding


class CreateTeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_create_team)
        binding = ActivityCreateTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        binding.txtCreateTeam.setOnClickListener {
            startActivity(Intent(this,CreateLeagueActivity::class.java))
        }
    }

    private fun setTopBar() {
        binding.headerinc.imgBack.visibility= View.VISIBLE
        binding.headerinc.imgBack.setOnClickListener { finish() }
        binding.headerinc.txtTitle.text=getString(R.string.create_team1)
    }
}