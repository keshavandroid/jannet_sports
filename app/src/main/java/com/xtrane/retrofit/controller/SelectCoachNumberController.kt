package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.bookinglistdata.BookingListBaseResponse
import com.xtrane.retrofit.coachteamdata.CoachTeamBaseResponse
import com.xtrane.viewinterface.IBokingListView
import com.xtrane.viewinterface.ICoachTeamListView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class SelectCoachNumberController(var context: Activity, internal var view: ICoachTeamListView):ICoachNumberSelectionController  {

    override fun callCoachNumberListApi(
        id: String,
        token: String,
        eventid: String,
        coach_id: String
    )
    {
        view.showLoader()

        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.SelectCoachNumber(id,token,eventid,coach_id)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack{
            override fun onSuccess(body: Response<ResponseBody?>?) {
                view.hideLoader()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(
                        Modifier.FINAL,
                        Modifier.TRANSIENT,
                        Modifier.STATIC)

                    val gson = builder.create()

                    val response = gson.fromJson(reader, CoachTeamBaseResponse::class.java)

                    if (response.getStatus() == 1) {

                        val data = response.getResult()
                        view.onCoachTeamListSuccess(data)

                    } else {
                        Log.d(ContentValues.TAG, "onSuccess: 0 status")
                        view.onFail(response.getMessage(), null)
                    }

                }catch (e: Exception) {
                    view.onFail(e.message!!, e)
                    e.printStackTrace()
                }            }

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