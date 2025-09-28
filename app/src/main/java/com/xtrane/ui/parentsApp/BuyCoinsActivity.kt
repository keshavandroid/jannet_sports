package com.xtrane.ui.parentsApp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.xtrane.R
import com.xtrane.databinding.ActivityBuyCoinsBinding
import com.xtrane.retrofit.APIClient.SERVER_URL
import com.xtrane.utils.Constants
import com.xtrane.utils.Constants.userType
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.io.Reader
import java.io.Serializable
import java.io.StringReader
import java.lang.ref.WeakReference
import java.lang.reflect.Modifier
import java.net.HttpURLConnection
import java.net.URL

class BuyCoinsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyCoinsBinding
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private var paymentIntentClientSecret: String? = null
    private var stripe: Stripe? = null
    private var httpClient: OkHttpClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyCoinsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

     //   val publishableKey = "pk_live_51RLyQDBuyQ1GNDW7vRJvbEgJsSEJz84SVekUx58lTBw92QE1yHfj0KvSy4Vb5R4VvUnA3NgO6Ju4EV08FLNOy1RE00Q8I4qqUq"
        val publishableKey = "pk_test_51RMt1EQOkb1porNanZl25YYGKxBAVSBAsYMixSUBNexFAk2VOJZYgmpVOGeie4VEsFh1E843XKHU3ot9wd8J7VJ500QXtihzAf"
        PaymentConfiguration.init(this@BuyCoinsActivity, publishableKey)

        setTopBar()
        setupClickListeners()
    }

    private fun setTopBar() {
        binding.topbarBalance.imgBack.setOnClickListener { onBackPressed() }
        binding.topbarBalance.txtTitle.text = getString(R.string.buy_coins)
    }

    private fun setupClickListeners() {
        binding.tvBuymore.setOnClickListener {
            val coinsToBuy = binding.tvCurrentBalance.text.toString().trim()

            if (TextUtils.isEmpty(coinsToBuy)) {
                Toast.makeText(this, "Please enter number of coins to buy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val coins = coinsToBuy.toInt()
                if (coins <= 0) {
                    Toast.makeText(this, "Please enter a valid number of coins", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                startCheckout(coins);


                // TODO: Implement payment processing
                // For now, we'll just return the coins value
//                val intent = Intent()
//                intent.putExtra("coins", coinsToBuy)
//                setResult(Activity.RESULT_OK, intent)
//                finish()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun startCheckout(coins: Int) {
        var userId=""
        lifecycleScope.launch {

            if (SharedPrefUserData(this@BuyCoinsActivity).getSavedData().usertype.equals(Constants.PARENT))
            {

                userId = SharedPrefUserData(this@BuyCoinsActivity).getSavedData().id.toString()
                Log.e("startCheckout", "userId: $userId")

            }
            if (SharedPrefUserData(this@BuyCoinsActivity).getSavedData().usertype.equals(Constants.COACH))
            {
                val storedata = StoreUserData(this@BuyCoinsActivity)

//                userID = storedata.getString(Constants.COACH_ID)
//                userToken = storedata.getString(Constants.COACH_TOKEN)

                userId =storedata.getString(Constants.COACH_ID)
                Log.e("startCheckout", "userId: $userId")

            }
            val response = withContext(Dispatchers.IO) {
                val url = URL(SERVER_URL +"createCoinPaymentIntent?userId="+userId+"&amount="+coins+"&currency=USD")
                Log.e("startCheckout", "url: $url")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"

                val stream = conn.inputStream.bufferedReader().use { it.readText() }
                JSONObject(stream)
            }
            Log.e("response=", "$response")

            paymentIntentClientSecret = response.getString("paymentIntentClientSecret")

            presentPaymentSheet()
        }
//        run {
//
//
//
//
//            val querystring = "https://www.x-trane.com/api/createPaymentIntent?" + "&userId=" + "1" + "&amount=" + coins + "&currency=usd"+ "&eventId=1"+ "&bookType=Bookevent"
//
//            ///  val json = Gson().toJson(payMap)
//            //val body: RequestBody = RequestBody.Companion.create(mediaType,json)
//            val request: Request = Request.Builder()
//                .url(querystring)
//                .post(null)
//                .build()
//
//            httpClient!!.newCall(request).enqueue(PayCallback(this))
//            activity.onPaymentSuccess(response)
//
//        }
    }
//    private class PayCallback internal constructor(activity: BuyCoinsActivity) : Callback {
//        private val activityRef: WeakReference<BuyCoinsActivity>
//
//        init {
//            activityRef = WeakReference<BuyCoinsActivity>(activity)
//        }
//
//        override fun onFailure(call: Call, e: IOException) {
//            val activity: BuyCoinsActivity = activityRef.get() ?: return
//            activity.runOnUiThread {
//                Toast.makeText(activity, "Error: $e", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        @Throws(IOException::class)
//        override fun onResponse(call: Call, response: Response) {
//            Log.e("response=1=", response.toString() + "===")
//
//            val activity: BuyCoinsActivity = activityRef.get() ?: return
//            if (!response.isSuccessful) {
//                activity.runOnUiThread {
//                    Toast.makeText(
//                        activity, "Error: $response", Toast.LENGTH_LONG
//                    ).show()
//                }
//            } else {
//                Log.e("response=2=", response.toString() + "===")
//
//                activity.onPaymentSuccess(response)
//            }
//        }
//    }
    private fun onPaymentSuccess(response: Response) {
        Log.e("onPaymentSuccess1=", "response=: ${response}")
        Log.e("onPaymentSuccess1=", "response.body=: ${response.body}")
        val resp = response!!.body!!.string()
        val reader: Reader = StringReader(resp)
        val builder = GsonBuilder()
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        val gson = builder.create()

        val result: Payment = gson.fromJson(reader, Payment::class.java)

        Log.e("onPaymentSuccess1=", "response.result=: ${result.resData}")



        paymentIntentClientSecret = result.resData!!.clientSecret!!



        presentPaymentSheet()


    }
    class Payment() : Serializable {
        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("result")
        @Expose
        var resData: Result? = null

        class Result() : Serializable {

            @SerializedName("paymentIntentId")
            @Expose
            var paymentIntentId: String? = null

            @SerializedName("clientSecret")
            @Expose
            var clientSecret: String? = null

//        @SerializedName("customer")
//        @Expose
//        var customer: String? = null

        }
    }
    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret!!,
            PaymentSheet.Configuration(
                merchantDisplayName = "Giovanni Trane",
                // Set `allowsDelayedPaymentMethods` to true if your business handles
                // delayed notification payment methods like US bank accounts.
                allowsDelayedPaymentMethods = true
            )
        )
    }
//    private fun presentPaymentSheet() {
//        val configuration = PaymentSheet.Configuration(
//            merchantDisplayName = "Your App",
//            customer = customerConfig
//        )
//
//        paymentIntentClientSecret?.let {
//            paymentSheet.presentWithPaymentIntent(it, configuration)
//        }
//    }
    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@BuyCoinsActivity, BalanceActivity::class.java)
                startActivity(intent)
                finish()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Error: ${paymentSheetResult.error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
} 