package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.addteamdata.AddTeamBaseResponse
import com.xtrane.retrofit.response.BasicResponse
import com.xtrane.viewinterface.IAddNewLocationView
import com.google.gson.GsonBuilder
import com.xtrane.viewinterface.IAddReportView
import com.xtrane.viewinterface.IRescheduleEventView
import com.xtrane.viewinterface.IRoasterFillingEventView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class RoasterFillingController(var context: Activity,
                               internal var view: IRoasterFillingEventView
) :
    IRosterFillingController {
    override fun CallEvent(
        id: String,
        token: String,
        eventID: String,
        coachID:String,
        date: String,
        time: String
    ) {
        view.showLoader()
        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? =
            apiInterface.addRosterFillingParticipant(id, token, eventID,coachID, date, time)


        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                view.hideLoader()
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

                    val response = gson.fromJson(reader, BasicResponse::class.java)

                    if (response.getStatus() == 1) {
                        view.onRosterEvent()

                    } else {
                        Log.d(ContentValues.TAG, "onSuccess: 0 status")
                        view.onFail(response.getMessage(), null)
                    }

                } catch (e: Exception) {
                    view.onFail(e.message!!, e)
                    e.printStackTrace()
                }
            }

            override fun onError(code: Int, error: String?) {
                view.hideLoader()
                Log.d(ContentValues.TAG, error + "")
                view.onFail(error!!, null)
            }

        })

    }

    override fun onDestroy() {
    }

    override fun onFinish() {
    }
}