package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityCreateLeagueBinding
import com.e.jannet_stable_code.databinding.ActivityEditMainTeamBinding


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