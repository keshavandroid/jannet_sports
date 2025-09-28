package com.xtrane.ui.parentsApp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xtrane.R
import com.xtrane.adapter.PurchaseHistoryAdapter
import com.xtrane.databinding.ActivityBalanceBinding
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.GetPurchaseHistoryController
import com.xtrane.retrofit.response.PurchaseHistoryResponse
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData

class BalanceActivity : AppCompatActivity(), ControllerInterface {
    private lateinit var binding: ActivityBalanceBinding
    var userToken = ""
    var userID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (SharedPrefUserData(this@BalanceActivity).getSavedData().usertype.equals(Constants.PARENT)) {

            userToken = SharedPrefUserData(this@BalanceActivity).getSavedData().token.toString()
            userID = SharedPrefUserData(this@BalanceActivity).getSavedData().id.toString()

            Log.e("BalanceActivity=parent", "userToken: $userToken")

        }
        if (SharedPrefUserData(this@BalanceActivity).getSavedData().usertype.equals(Constants.COACH)) {

            val storedata = StoreUserData(this@BalanceActivity)

            userID = storedata.getString(Constants.COACH_ID)
            userToken = storedata.getString(Constants.COACH_TOKEN)

            Log.e("BalanceActivity=coach", "userToken: $userToken")

        }

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

        GetPurchaseHistoryController(this@BalanceActivity, this).callApi(userID, userToken)
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

    override fun onFail(error: String?) {
        Log.e("onFail=", error.toString())
    }

    override fun <T> onSuccess(response: T, method: String) {
        if (method.equals("GetPurchaseHistoryCont")) {
            val responsehistory: PurchaseHistoryResponse = response as PurchaseHistoryResponse

            binding.tvCurrentBalance.text = responsehistory.getResult()!!.currentBalance
            binding.tvUsedBalance.text = responsehistory.getResult()!!.usedBalance
            binding.tvTotalBalance.text = responsehistory.getResult()!!.totalBalance

            binding.rvPurchases.layoutManager = LinearLayoutManager(this)
            binding.rvPurchases.adapter =
                PurchaseHistoryAdapter(responsehistory.getResult()!!.history, this)
        }
    }
} 