package com.xtrane.ui.parentsApp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xtrane.R
import com.xtrane.databinding.ActivityBalanceBinding
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData

class BalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBalanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()
        setupClickListeners()
        loadBalanceData()
    }

    private fun setTopBar() {
        binding.topbarBalance.imgBack.setOnClickListener { onBackPressed() }
        binding.topbarBalance.txtTitle.text = getString(R.string.wallet)
    }

    private fun setupClickListeners() {
        binding.tvBuymore.setOnClickListener {
            val intent = Intent(this, BuyCoinsActivity::class.java)
            startActivityForResult(intent, REQUEST_BUY_COINS)
        }
    }

    private fun loadBalanceData() {
        // TODO: Load balance data from API or local storage
        val currentBalance = "100" // Example value
        val usedBalance = "50" // Example value
        
        binding.tvCurrentBalance.text = currentBalance
        binding.tvUsedBalance.text = usedBalance
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_BUY_COINS && resultCode == Activity.RESULT_OK) {
            val newCoins = data?.getStringExtra("coins")
            // TODO: Update balance with new coins
            loadBalanceData()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val REQUEST_BUY_COINS = 1001
    }
} 