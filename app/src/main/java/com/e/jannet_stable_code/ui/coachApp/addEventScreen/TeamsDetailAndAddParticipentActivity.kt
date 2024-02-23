package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import kotlinx.android.synthetic.main.activity_teams_detail_and_add_participent.*

class TeamsDetailAndAddParticipentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams_detail_and_add_participent)



        tv_add_member.setOnClickListener {


            val eventid = intent.getStringExtra("EVENT_ID")
            val team_id = intent.getStringExtra("TEAM_ID")

            val intent = Intent(this, TeamsDetailAndAddParticipentActivity::class.java)
            intent.putExtra("EVENT_ID", eventid.toString())
            intent.putExtra("TEAM_ID", team_id.toString())
            startActivity(intent)

        }


        tv_view_detail.setOnClickListener {

            val eventid = intent.getStringExtra("EVENT_ID")
            val team_id = intent.getStringExtra("TEAM_ID")


            val intent = Intent(this, TeamsDetailAndAddParticipentActivity::class.java)
            intent.putExtra("EVENT_ID", eventid.toString())
            intent.putExtra("TEAM_ID", team_id.toString())

            startActivity(intent)



        }

    }
}