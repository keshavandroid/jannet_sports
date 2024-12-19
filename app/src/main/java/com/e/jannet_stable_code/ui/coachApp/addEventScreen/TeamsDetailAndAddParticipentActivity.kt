package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityTeamsDetailAndAddParticipentBinding
import com.e.jannet_stable_code.databinding.ActivityTickitsCoachBinding

class TeamsDetailAndAddParticipentActivity : AppCompatActivity() {

    private lateinit var bind: ActivityTeamsDetailAndAddParticipentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_teams_detail_and_add_participent)
        bind = ActivityTeamsDetailAndAddParticipentBinding.inflate(layoutInflater)
        setContentView(bind.root)


        bind.tvAddMember.setOnClickListener {


            val eventid = intent.getStringExtra("EVENT_ID")
            val team_id = intent.getStringExtra("TEAM_ID")

            val intent = Intent(this, TeamsDetailAndAddParticipentActivity::class.java)
            intent.putExtra("EVENT_ID", eventid.toString())
            intent.putExtra("TEAM_ID", team_id.toString())
            startActivity(intent)

        }


        bind.tvViewDetail.setOnClickListener {

            val eventid = intent.getStringExtra("EVENT_ID")
            val team_id = intent.getStringExtra("TEAM_ID")


            val intent = Intent(this, TeamsDetailAndAddParticipentActivity::class.java)
            intent.putExtra("EVENT_ID", eventid.toString())
            intent.putExtra("TEAM_ID", team_id.toString())

            startActivity(intent)



        }

    }
}