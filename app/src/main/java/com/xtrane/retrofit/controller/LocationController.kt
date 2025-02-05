package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetroHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.locationdata.LocationBaseResponse
import com.xtrane.viewinterface.ILocationView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

const val Location = "TAG"
class LocationController(var context: Activity, internal var view: ILocationView):ILocationController {

    override fun callLocationApi(id: String, token: String) {
        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.getLocation(id,token)
        Log.e(Location, "callLocationApi:>>1 " )
        RetroHelper.callApi(call, object : RetroHelper.ResponseHandler{
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Log.e(Location, "callLocationApi:>>2 success " )

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

                    val response = gson.fromJson(reader, LocationBaseResponse::class.java)

                    if (response.getStatus() == 1) {
                        Log.e(Location, "callLocationApi:>>1 success status 1 " )

                        var data = response.getResult()
                        Log.e(Location, "callLocationApi:>>3"+data )

                        var coat =  response.getResult()!![1]?.getCoat()
                        Log.e(Location, "callLocationApi:>>4"+coat )

                        view.onLocationListSuccess(data!!)
                        view.onCoatListSuccess(coat)

                    } else {
                        Log.d(ContentValues.TAG, "onSuccess: 0 status")
                        view.onFail(response.getMessage(), null)

                        Log.e(Location, "onSuccess: >>5 status 0" )
                    }

                }catch (e: Exception) {
                    view.onFail(e.message!!, e)
                    e.printStackTrace()
                    Log.e(Location, "onSuccess: >>6 exceprtion $e" )

                }
            }

            override fun onError(code: Int, error: String?) {
                view.hideLoader()
                Log.d(ContentValues.TAG, error + "")
                view.onFail(error!!, null)

                Log.e(Location, "onSuccess: >>7 error  $code ,==========$error" )

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