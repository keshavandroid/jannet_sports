package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import kotlinx.android.synthetic.main.activity_add_match.*
import kotlinx.android.synthetic.main.topbar_layout.*

class AddMatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match)

        setTopBar()

        txtAdd.setOnClickListener {
            startActivity(Intent(this,
            CoachMainActivity::class.java))
        }
    }

    private fun setTopBar() {
        imgBack.visibility= View.VISIBLE
        imgBack.setOnClickListener { finish() }
        txtTitle.text=getString(R.string.add_match1)
    }
}