package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.response.PrivacyPolicyResponse
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

private const val TAG = "SaveSportListCont"
class SaveSportListController(val context: Activity, val sportsId:String, internal val controllerInterface: ControllerInterface) {
    init {
        callApi()
    }
    private fun callApi() {

        val storeData = StoreUserData(context)
        val id=storeData.getString(Constants.COACH_ID)
        val token=storeData.getString(Constants.COACH_TOKEN)

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().saveSportListApi(id,token,sportsId)

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
                    val response: PrivacyPolicyResponse = gson.fromJson(reader, PrivacyPolicyResponse::class.java)
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
                        SharedPrefUserData(context).setRegisterStep("3")
                        controllerInterface.onSuccess(response, "SaveResp")
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