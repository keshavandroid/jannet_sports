package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.GetChildGroupListResponse
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class GetChildGroupListController(var context: Activity, internal var controllerInterface: ControllerInterface) {
    private val TAG = "GetChildGroupListCont"
    fun callApi() {
        Utilities.showProgress(context)

//        val id= SharedPrefUserData(context).getSavedData().id
//        val token= SharedPrefUserData(context).getSavedData().token

        val stordata = StoreUserData(context)
        val id = stordata.getString(Constants.COACH_ID)
        val token = stordata.getString(Constants.COACH_TOKEN)
        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().childGroupList(id,token)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: success>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: GetChildGroupListResponse = gson.fromJson(reader, GetChildGroupListResponse::class.java)
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
                        controllerInterface.onSuccess(response, "GetSportsListCont")
                    } else {
                        Log.d(TAG, "onSuccess: 0 status")
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