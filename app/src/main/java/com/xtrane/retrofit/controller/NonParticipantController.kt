package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.nonparticipantdata.NonParticipantBaseResponse
import com.xtrane.retrofit.teamdetaildata.TeamDetailBaseResponse
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.INonParticipanView
import com.xtrane.viewinterface.TeamDetailView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class NonParticipantController(var context: Activity, internal var view: INonParticipanView) :
    INonParticipantController {
    override fun callNonParticipantApi(id: String, token: String, event_id: String) {
        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)

        val call: Call<ResponseBody?>? = apiInterface.getParticipant(id, token, event_id)
        // val call: Call<ResponseBody?>? = apiInterface.getNonTeamParticipants(id, token,event_id)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(
                        Modifier.FINAL,
                        Modifier.TRANSIENT,
                        Modifier.STATIC
                    )
                    val gson = builder.create()
                    val response: NonParticipantBaseResponse =
                        gson.fromJson(reader, NonParticipantBaseResponse::class.java)

                    if (response.getStatus() == 1) {
                        val data = response.getResult()!!

                        view.onNonParticipantSuccess(data)

                        Utilities.showToast(context, response.getMessage())

                    } else
                    {
                        Utilities.showToast(context, response.getMessage())

                        view.onFail(response.getMessage(), null)

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun onError(code: Int, error: String?) {
                Log.d("TAG", "onError: ===" + error)
                //                Utils.showAlert((Activity) activity, error);
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