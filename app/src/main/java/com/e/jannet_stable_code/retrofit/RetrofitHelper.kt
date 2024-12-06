package com.e.jannet_stable_code.retrofit

import android.accounts.NetworkErrorException
import android.util.Log
import com.android.volley.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object RetrofitHelper {

//    private val SERVER_URL = "https://keshavinfotechdemo2.com/keshav/KG2/Jannet_new/api/"
    private val SERVER_URL = "http://www.x-trane.com/api/"

    private var gsonAPI: UserServices? = null
    private var connectionCallBack: ConnectionCallBack? = null

    fun getAPI(): UserServices {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val arraList=ArrayList<ConnectionSpec>()
        arraList.add(ConnectionSpec.CLEARTEXT)
        arraList.add(ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .allEnabledTlsVersions()
            .allEnabledCipherSuites()
            .build())

        val okHttpClient = OkHttpClient.Builder()
            .connectionSpecs(arraList)
            .addInterceptor(logging)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("yyyy-MM-dd")
        gsonBuilder.disableHtmlEscaping()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build()

        return retrofit.create(UserServices::class.java)
    }

    fun getAPI(serverUrl: String): UserServices {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG)
            okHttpClient.addInterceptor(logging)

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat("yyyy-MM-dd")
        gsonBuilder.disableHtmlEscaping()
        val timeout = 1 * 60 * 1000

        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .client(
                okHttpClient.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
                    .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(timeout.toLong(), TimeUnit.SECONDS).build()
            )
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build()

        return retrofit.create(UserServices::class.java)
    }

    private fun clearConnectionCallback() {
        if (masterCall != null)
            masterCall!!.cancel()
        connectionCallBack = null
    }

    private var masterCall: Call<ResponseBody?>? = null
    fun callApi(call: Call<ResponseBody?>?, callBack: ConnectionCallBack?) {
        clearConnectionCallback()
        connectionCallBack = callBack

        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) { //                Utils.appendLog(response.raw().request().url() + "  receive response: " + response.raw().receivedResponseAtMillis() + " send= " + response.raw().sentRequestAtMillis());
//                Utils.appendLog(response.raw().request().url() + "  diff= " + (response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis()));
                if (response.code() == 200) {
                    if (connectionCallBack != null) connectionCallBack!!.onSuccess(response)
                } else {
                    try {
                        var res = response.errorBody()!!.string()
                        if (response.code() == 400) {
                            res = " Bad request"
                        }

                        if (connectionCallBack != null) connectionCallBack!!.onError(
                            response.code(),
                            res
                        )
                    } catch (e: IOException) {
                        Log.i("TAG", "onResponse: " + call.request().url)
                        e.printStackTrace()
                        if (connectionCallBack != null) connectionCallBack!!.onError(
                            response.code(),
                            e.message
                        )
                    } catch (e: NullPointerException) {
                        Log.i("TAG", "onResponse: " + call.request().url)
                        e.printStackTrace()
                        if (connectionCallBack != null) connectionCallBack!!.onError(
                            response.code(),
                            e.message
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, error: Throwable) {
                var message: String? = null
                if (error is NetworkErrorException || error is ConnectException) {
                    message = "Please check your internet connection"
                } else if (error is ParseException) {
                    message = "Parsing error! Please try again after some time!!"
                } else if (error is TimeoutException) {
                    message = "Connection TimeOut! Please check your internet connection."
                } else if (error is UnknownHostException) {
                    message = "Please check your internet connection and try later"
                } else if (error is Exception) {
                    message = error.message
                }
                if (connectionCallBack != null) connectionCallBack!!.onError(-1, message)
            }
        })
        masterCall = call
    }

    interface ConnectionCallBack {
        fun onSuccess(body: Response<ResponseBody?>?)
        fun onError(code: Int, error: String?)
    }
}