package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.matchlistdata.MatchListBaseResponse
import com.xtrane.retrofit.minmaxagedata.MinMaxAgeBaseResponse
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.IMatchListView
import com.xtrane.viewinterface.IMinMAxAgeView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class MinMaxAgeController(var context: Activity, internal var view: IMinMAxAgeView) :
    IMinMaxAgeController {
    override fun callMinMaxAgeApi(id: String, token: String) {
        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.getMiMaxAge(id, token)
        view.showLoader()

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
                    val response: MinMaxAgeBaseResponse =
                        gson.fromJson(reader, MinMaxAgeBaseResponse::class.java)

                    if (response.getStatus() == 1) {
                        val data = response.getResult()!!

                        view.hideLoader()

                        view.onMinMaxAgeSuccess(data)
                        Utilities.showToast(context, response.getMessage())

                    } else {

                        view.hideLoader()

                        Utilities.showToast(context, response.getMessage())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    view.hideLoader()

                }
            }


            override fun onError(code: Int, error: String?) {
                Log.d("TAG", "onError: ===" + error)
                //                Utils.showAlert((Activity) activity, error);
                view.hideLoader()

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