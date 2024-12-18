package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddTeamsBinding
import com.e.jannet_stable_code.databinding.ActivityTeamsBinding
import com.e.jannet_stable_code.databinding.ActivityVenueBinding


class AddTeamsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddTeamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_teams)
        binding = ActivityAddTeamsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includeAddTeams.txtTitle.text="ADD TEAM"
        binding.includeAddTeams.imgBack.setOnClickListener {

            onBackPressed()
        }

        val bundle = intent.extras
        val eventid = bundle!!.getString("eid")
//val eventid = intent.getStringExtra("eid")
        Log.e("TAG", "onCreate:addteam view event id ==$eventid ")
        binding.txtAddTeamsEvent.setOnClickListener {

            val intent = Intent(this, AddTeamsFinalActivity::class.java)
            intent.putExtra("EVENT_ID",eventid.toString())
            startActivity(intent)
            finish()
        }
    }


    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Home Screen")
        //set message for alert dialog
        builder.setMessage("Are you Sure you Want to Go Home Screen?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            Toast.makeText(applicationContext,"clicked yes", Toast.LENGTH_LONG).show()

            super.onBackPressed()


        }
        //performing cancel action
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(applicationContext,"clicked cancel\n operation cancel", Toast.LENGTH_LONG).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(applicationContext,"clicked No", Toast.LENGTH_LONG).show()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

}