package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import kotlinx.android.synthetic.main.activity_create_team.*
import kotlinx.android.synthetic.main.topbar_layout.*

class CreateTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)

        setTopBar()

        txtCreateTeam.setOnClickListener {
            startActivity(Intent(this,CreateLeagueActivity::class.java))
        }
    }

    private fun setTopBar() {
        imgBack.visibility= View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text=getString(R.string.create_team1)
    }
}