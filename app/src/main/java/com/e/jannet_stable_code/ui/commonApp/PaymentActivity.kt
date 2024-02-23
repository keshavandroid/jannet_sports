package com.e.jannet_stable_code.ui.commonApp

import android.os.Bundle
import android.util.Log
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.controller.IBaseController
import com.e.jannet_stable_code.retrofit.response.PaymentIntentResponse
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.Utilities
import com.google.gson.GsonBuilder
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier
import java.util.*


class PaymentActivity : BaseActivity() {

    lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String
    private var stripe: Stripe? = null

    override fun getController(): IBaseController? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        //Initialize
        stripe = Stripe(
            applicationContext,
            Objects.requireNonNull("pk_test_51H3JJaGn6y9km7y5YIm5ViSBd5lzZiQ0oAILi5Q474qkORnDygMgkIlTskltweM6T3iAYmm1LZWp9U14XX0mZLWb00Ilcqp6il")
        )
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)


        //IOS key
//        pk_test_51K8eLiSDzc4Jch3XL6H2SleBW7cWSACBXHV5uFV921Vjuffsk2oJZS1FnCSpkDIhY38GC7vqfNyeWY7FimbTJZ7V00rXtHqgP5

        /*"Your backend endpoint/payment-sheet".httpPost().responseJson { _, _, result ->
            if (result is Success) {
                val responseJson = result.get().obj()
                paymentIntentClientSecret = responseJson.getString("paymentIntent")
                customerConfig = PaymentSheet.CustomerConfiguration(
                    responseJson.getString("customer"),
                    responseJson.getString("ephemeralKey")
                )
                val publishableKey = responseJson.getString("publishableKey")
                PaymentConfiguration.init(this, publishableKey)
            }
        }*/

        fetchPaymentIntentApi()

    }

    private fun fetchPaymentIntentApi() {


        val id = SharedPrefUserData(this).getSavedData().id
        val eventid = intent.getStringExtra("eventId")

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().createPaymentIntentApi("50","USD",eventid,id,"Bookevent")

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: PaymentIntentResponse = gson.fromJson(reader, PaymentIntentResponse::class.java)

                    if (response.getStatus()== 1) {
                        val data = response.getMessage()
                        Utilities.showToast(this@PaymentActivity, response.getMessage())

                        if(!response.getPaymentIntentClientSecret().isNullOrEmpty()){
                            PaymentConfiguration.init(this@PaymentActivity, response.getPaymentIntentClientSecret().toString())
                        }
                    } else {
                        Utilities.showToast(this@PaymentActivity, response.getMessage())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Log.d("TAG", "onError: ===" + error)
            }
        })

    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
            }
        }
    }

    fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business
                // can handle payment methods that complete payment after a delay, like SEPA Debit and Sofort.
                allowsDelayedPaymentMethods = true
            )
        )
    }



}