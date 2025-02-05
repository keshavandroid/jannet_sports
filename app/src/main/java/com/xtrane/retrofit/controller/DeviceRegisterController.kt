package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.addteamdata.AddTeamBaseResponse
import com.xtrane.viewinterface.IAddMatchView
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class DeviceRegisterController(var context: Activity, internal var view: IAddMatchView):IDeviceRegisterController {
    override fun callDeviceRegister(
        regId: String,
        user_id: String,
        device_id:String,
        email:String
    ) {


//        view.showLoader()
        var jsonObject = JsonObject()
        jsonObject.addProperty("regId", regId)
        jsonObject.addProperty("user_id", user_id)
        jsonObject.addProperty("device_id", device_id)
        jsonObject.addProperty("email", email)



        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.deviceRegisterNoti(jsonObject)


        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack{
            override fun onSuccess(body: Response<ResponseBody?>?) {
//                view.hideLoader()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(
                        Modifier.FINAL,
                        Modifier.TRANSIENT,
                        Modifier.STATIC)

                    val gson = builder.create()

                    /*val response = gson.fromJson(reader, AddTeamBaseResponse::class.java)

                    if (response.getStatus() == 1) {
                        view.addMatchSuccessful()

                    } else {
                        Log.d(ContentValues.TAG, "onSuccess: 0 status")
                        view.onFail(response.getMessage(), null)
                    }*/

                }catch (e: Exception) {
                    view.onFail(e.message!!, e)
                    e.printStackTrace()
                }
            }

            override fun onError(code: Int, error: String?) {
//                view.hideLoader()
                Log.d(ContentValues.TAG, error + "")
                view.onFail(error!!, null)
            }

        })


    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun onFinish() {
        TODO("Not yet implemented")
    }
}