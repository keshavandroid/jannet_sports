package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.SportsListResponse
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

class GetSportsListController(var context: Activity, internal var controllerInterface: ControllerInterface) {
    private val TAG = "GetSportsListCont"
    var id = ""
    var token = ""
    fun callApi() {

        Utilities.showProgress(context)

        val data = SharedPrefUserData(context).getSavedData()
        val storData = StoreUserData(context)

//        val id= SharedPrefUserData(context).getSavedData().id
//        val token= SharedPrefUserData(context).getSavedData().token
        if (storData.getString(Constants.COACH_ID).trim().isEmpty()||storData.getString(Constants.COACH_ID).trim()==""||storData.getString(Constants.COACH_ID).trim()=="") {

            id = data.id.toString()
            token = data.token.toString()

        }else{

             id = storData.getString(Constants.COACH_ID)
             token = storData.getString(Constants.COACH_TOKEN)

        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().getSportList(id,token)

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
                    val response: SportsListResponse = gson.fromJson(reader, SportsListResponse::class.java)
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
                Log.d(TAG, "onError: ===="+error.toString())
                controllerInterface.onFail(error)
            }
        })
    }
}