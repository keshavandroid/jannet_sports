package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.ImageListObject
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.EventDetailResponse
import com.xtrane.ui.loginRegister.addChildScreen.AddChildActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class EventDetailController(var context: Activity, internal var controllerInterface: ControllerInterface) {
    private val TAG = "EventDetailCont"
    private  var id = ""
    private var token =""
    fun callApi(eventId: String) {
       // Utilities.showProgress(context)

        val imgList=ArrayList<String>()
        val imgId=ArrayList<String>()

        val finalImages=ArrayList<ImageListObject>()

        val storedata = StoreUserData(context)

        if (storedata.getString(Constants.COACH_ID).trim().isEmpty() || storedata.getString(Constants.COACH_ID).trim()==""){

            id = SharedPrefUserData(context).getSavedData().id!!
            token = SharedPrefUserData(context).getSavedData().token!!
            val userType: String = SharedPrefUserData(context).getSavedData().usertype!!

            Log.e(TAG, "callApi: parent id token is $id", )

        }
        else {


            id = storedata.getString(Constants.COACH_ID)
            token = storedata.getString(Constants.COACH_TOKEN)

            Log.e(TAG, "callApi: coac id token is $id", )


        }


//        val id= SharedPrefUserData(context).getSavedData().id
//        val token= SharedPrefUserData(context).getSavedData().token

//        val stordata = StoreUserData(context)
//        val id = stordata.getString(Constants.COACH_ID)
//        val token = stordata.getString(Constants.COACH_TOKEN)
        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().eventDetail(id,token,eventId)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
               // Utilities.dismissProgress()
                Log.d(TAG, "RetrofitHelper: insuccess>>$body<")

                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: insuccess>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: EventDetailResponse = gson.fromJson(reader, EventDetailResponse::class.java)
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")

                    if (response.getStatus() == 1) {

                        controllerInterface.onSuccess(response, "AddChildSuccess")
                    }
                    else {
                        Log.d(TAG, "onSuccess: 0 status")
                        Utilities.showToast(context, response.getMessage())
                        controllerInterface.onFail(response.getMessage())
                    }
                }
                catch (e: Exception) {
                 //   Utilities.dismissProgress()
                    Log.d(TAG, "onSuccess: insuccess>>" + e.message + "<")
                    Utilities.showToast(context, "Something went wrong.")
                    e.printStackTrace()
                    controllerInterface.onFail(e.message)
                }
            }


            override fun onError(code: Int, error: String?) {
               // Utilities.dismissProgress()
                //Utilities.showToast(context, "Server error")
                Log.d(TAG, "onError: ====")
                controllerInterface.onFail(error)
            }
        })
    }
}