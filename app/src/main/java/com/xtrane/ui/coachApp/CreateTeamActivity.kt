package com.xtrane.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xtrane.R
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityCreateTeamBinding
import com.xtrane.databinding.ActivityTeamsBinding


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