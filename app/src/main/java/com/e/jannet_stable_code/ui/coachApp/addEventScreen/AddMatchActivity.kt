package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMatch2Binding
import com.e.jannet_stable_code.databinding.ActivityVenueBinding


class AddMatchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddMatch2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_match2)
        binding = ActivityAddMatch2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topbar.txtTitle.text="ADD Match"
        binding.topbar.imgBack.setOnClickListener {

            onBackPressed()
        }

        binding.txtAddMatchEvent.setOnClickListener {
            val event_id = intent.getStringExtra("EVENT_ID")

            val intent = Intent(this, AddMatchFActivity::class.java)
            intent.putExtra("EVENT_ID",event_id.toString())
            startActivity(intent)
                finish()
        }
    }
}