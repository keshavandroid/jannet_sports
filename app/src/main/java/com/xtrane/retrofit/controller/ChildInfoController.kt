package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.childinfodata.ChildInfoBaseResponse
import com.xtrane.viewinterface.IChildInfoView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class ChildInfoController(var context: Activity, internal var view: IChildInfoView):IChildInfoController {
    override fun callChildInfoAPI(id: String, token: String, childIds: String) {

        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.getChildInfo(id,token,childIds)


        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack{
            override fun onSuccess(body: Response<ResponseBody?>?) {
                view.hideLoader()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL,
                        Modifier.TRANSIENT,
                        Modifier.STATIC)

                    val gson = builder.create()

                    val response = gson.fromJson(reader, ChildInfoBaseResponse::class.java)

                    if (response.getStatus() == 1) {
                        var data = response.getResult()
                        view.onChildInfoSuccess(data)

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
        TODO("Not yet implemented")
    }

    override fun onFinish() {
        TODO("Not yet implemented")
    }
}