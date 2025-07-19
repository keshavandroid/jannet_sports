package com.xtrane.retrofit.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.EventListResponse
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class PFilterEventListController(var context: Activity,var strIDSports:String,
                                 var strIDLocation:String,
                                 var startDate:String,
                                 var endDate:String,
                                 internal var controllerInterface: ControllerInterface) {
    private val TAG = "PFilterEventListController"
    fun callApi() {
        Utilities.showProgress(context)

        val storeData = StoreUserData(context)

        val id= SharedPrefUserData(context).getSavedData().id
        val token= SharedPrefUserData(context).getSavedData().token

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().parentHomeFilter(id,token,strIDLocation,strIDSports,startDate,endDate,"")

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            @SuppressLint("LongLogTag")
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: insuccess>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: EventListResponse = gson.fromJson(reader, EventListResponse::class.java)
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
                        controllerInterface.onSuccess(response, "AddChildSuccess")
                    } else {
                        Log.d(TAG, "onSuccess: 0 status")
                        Utilities.showToast(context, response.getMessage())
                        controllerInterface.onFail(response.getMessage())
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: insuccess>>" + e.message + "<")
                    Utilities.showToast(context, "Something went wrong.")
                    e.printStackTrace()
                    controllerInterface.onFail(e.message)
                }
            }


            @SuppressLint("LongLogTag")
            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Utilities.showToast(context, "Server error")
                Log.d(TAG, "onError: ====")
                controllerInterface.onFail(error)
            }
        })
    }
}