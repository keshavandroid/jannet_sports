package com.xtrane.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xtrane.R
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityCreateLeagueBinding
import com.xtrane.databinding.ActivityEditMainTeamBinding


class CreateLeagueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateLeagueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_league)
        binding = ActivityCreateLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        binding.txtCreateTeam.setOnClickListener {
            startActivity(Intent(this,CoachMainActivity::class.java))
            finish()
        }
    }

    private fun setTopBar() {
        binding.includeTopbar.imgBack.visibility= View.VISIBLE
        binding.includeTopbar.imgBack.setOnClickListener { finish() }
        binding.includeTopbar.txtTitle.text=getString(R.string.create_league)
    }
}