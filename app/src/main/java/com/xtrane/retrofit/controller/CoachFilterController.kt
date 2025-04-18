package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.coachfilterdata.CoachFilterBaseresponse
import com.xtrane.retrofit.coachsportslistdata.CoachSportsListBaseResponse
import com.xtrane.viewinterface.ICoachFilterView
import com.xtrane.viewinterface.ICoachSportsListVIew
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class CoachFilterController(var context: Activity, internal var view: ICoachFilterView) :
    ICoachFilterController  {
    override fun callCoachFilterApi(
        id: String,
        token: String,
        location_id: String,
        sportsId: String
    ) {

        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.getCoachFilter(id, token, location_id,sportsId)
        view.showLoader()

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {


            override fun onSuccess(body: Response<ResponseBody?>?) {
                Log.e("Sports", "callCoachSportsListApi:>>2 ", )

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

                    val response = gson.fromJson(reader, CoachFilterBaseresponse::class.java)

                    if (response.getStatus() == 1) {
                        Log.e("Sports", "callCoachSportsListApi:>>3 ", )

                        val data = response.getResult()
                        view.onCoachFilterSuccess(data)
                        view.hideLoader()

                    } else {
                        Log.e("Sports", "callCoachSportsListApi:>>4 ", )

                        Log.d(ContentValues.TAG, "onSuccess: 0 status")
                        view.onFail(response.getMessage(), null)
                        view.hideLoader()
                    }

                } catch (e: Exception) {
                    Log.e("Sports", "callCoachSportsListApi:>>5 ", )

                    view.hideLoader()
                    view.onFail(e.message!!, e)
                    e.printStackTrace()
                }
            }

            override fun onError(code: Int, error: String?) {
                Log.e("Sports", "callCoachSportsListApi:>>6 ", )

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