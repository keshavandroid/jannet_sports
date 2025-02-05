package com.xtrane.ui.coachApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xtrane.R
import com.xtrane.databinding.ActivityAddMainTeamBinding
import com.xtrane.databinding.ActivityAddMatchBinding


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