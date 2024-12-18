package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.databinding.ActivityAddMainTeamBinding
import com.e.jannet_stable_code.databinding.ActivityAddMatchBinding


class AddMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_add_match)
        binding = ActivityAddMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTopBar()

        binding.txtAdd.setOnClickListener {
            startActivity(Intent(this,
            CoachMainActivity::class.java))
        }
    }

    private fun setTopBar() {
        binding.topbar.imgBack.visibility= View.VISIBLE
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text=getString(R.string.add_match1)
    }
}