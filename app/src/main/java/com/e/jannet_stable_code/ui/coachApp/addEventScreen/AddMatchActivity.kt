package com.e.jannet_stable_code.ui.coachApp.addEventScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.jannet_stable_code.R
import kotlinx.android.synthetic.main.activity_add_match2.*
import kotlinx.android.synthetic.main.topbar_layout.*

class AddMatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match2)

        txtTitle.text="ADD Match"
        imgBack.setOnClickListener {

            onBackPressed()
        }

        txtAdd_match_event.setOnClickListener {
            val event_id = intent.getStringExtra("EVENT_ID")

            val intent = Intent(this, AddMatchFActivity::class.java)
            intent.putExtra("EVENT_ID",event_id.toString())
            startActivity(intent)
                finish()
        }
    }
}