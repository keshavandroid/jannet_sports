package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.util.Log
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListBaseResponse
import com.e.jannet_stable_code.retrofit.response.EventListResponse
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class GetParentMatchController (var context: Activity, internal var controllerInterface: ControllerInterface){
    private val TAG = "GetParentMatch"
    fun callApi() {
        Utilities.showProgress(context)

        val storeData = StoreUserData(context)

        val id= SharedPrefUserData(context).getSavedData().id
        val token= SharedPrefUserData(context).getSavedData().token

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().getUserMatchHistory(id,token)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: insuccess>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: MatchListBaseResponse = gson.fromJson(reader, MatchListBaseResponse::class.java)
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
                        controllerInterface.onSuccess(response.getResult(), "AddChildSuccess")
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


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Utilities.showToast(context, "Server error")
                Log.d(TAG, "onError: ====")
                controllerInterface.onFail(error)
            }
        })
    }
}