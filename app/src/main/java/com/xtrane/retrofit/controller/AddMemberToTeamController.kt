package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.CoachRegisterResponse
import com.xtrane.ui.parentsApp.RegisterData
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.RegisterControllerInterface
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class AddMemberToTeamController(var context: Activity, var registerControllerInterface: RegisterControllerInterface) {


    fun addTeamMember(event_id:String, member_id:String, team_id:String) {

        val storedata = StoreUserData(context)
        val idd = storedata.getString(Constants.COACH_ID)
        val tokenn = storedata.getString(Constants.COACH_TOKEN)


        val id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, idd)
        val token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,tokenn)
        val eventid: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,event_id)
        val memberid: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,member_id)
        val teamid: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,team_id)


        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().addTeamMember(id,token,eventid,memberid,teamid)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d("TAG", "onSuccess: insuccess>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(
                        Modifier.FINAL,
                        Modifier.TRANSIENT,
                        Modifier.STATIC
                    )
                    val gson = builder.create()
                    val response: CoachRegisterResponse =
                        gson.fromJson(reader, CoachRegisterResponse::class.java)
                    Log.d("TAG", "onSuccess: insuccess>>" + response.getStatus() + "<")

                    if (response.getStatus() == 1) {
//                        SharedPrefUserData(context).saveRegisterCoach(response.getResult())
//                        SharedPrefUserData(context).setRegisterStep("1")
                        registerControllerInterface.onSuccess(response)
                    }
                    else {
                        Log.d("TAG", "onSuccess: 0 status")
                        Utilities.showToast(context, response.getMessage())
                        registerControllerInterface.onFail(response.getMessage())
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "onSuccess: insuccess>>" + e.message + "<")
                    Utilities.showToast(context, "Something went wrong.")
                    e.printStackTrace()
                    registerControllerInterface.onFail(e.message)
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Utilities.showToast(context, "Server error")
                Log.d("TAG", "onError: ====")
                registerControllerInterface.onFail(error)
            }
        })


    }

}