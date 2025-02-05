package com.xtrane.retrofit

import android.accounts.NetworkErrorException
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

object RetroHelper
{
    fun getAPI(): UserServices {
        var apiInterface = APIClient.getClient()!!.create(UserServices::class.java)
        return apiInterface
    }

    fun callApi(call: Call<ResponseBody?>?, callBack: ResponseHandler?) {
//        clearConnectionCallback()

//        connectionCallBack = callBack

        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.code() == 200) {
                    if (callBack != null) callBack!!.onSuccess(response)
                } else {
                    try {
                        var res = response.errorBody()!!.string()
                        if (response.code() == 400) {
                            res = " Bad request"
                        }

                        if (callBack != null) callBack!!.onError(
                            response.code(),
                            res
                        )
                    } catch (e: IOException) {
                        Log.i("TAG", "onResponse: " + call.request().url)
                        e.printStackTrace()
                        if (callBack != null) callBack!!.onError(
                            response.code(),
                            e.message
                        )
                    } catch (e: NullPointerException) {
                        Log.i("TAG", "onResponse: " + call.request().url)
                        e.printStackTrace()
                        if (callBack != null) callBack!!.onError(
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
                if (callBack != null) callBack!!.onError(-1, message)
            }
        })
//        masterCall = call
    }

    interface ResponseHandler {
        fun onSuccess(body: Response<ResponseBody?>?)
        fun onError(code: Int, error: String?)
    }
}