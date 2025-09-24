package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.deleteteam.DeleteTeam
import com.xtrane.viewinterface.IDeleteTeamView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class DeleteMemberController(var context: Activity, internal var view: IDeleteTeamView):IDeleteMemberController {

    override fun callDeleteMemberApi(id: String,token:String,memberId:String,eventId:String,teamId:String) {

        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)

        val call: Call<ResponseBody?>? = apiInterface.deleteTeamMember(id,token,memberId,eventId,teamId)

        view.showLoader()

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

                    val response = gson.fromJson(reader, DeleteTeam::class.java)

                    if (response.getStatus() == 1) {
                        view.deleteTeamSuccessful()
                        view.hideLoader()

                    } else {
                        Log.d(ContentValues.TAG, "onSuccess: 0 status")
                        view.onFail(response.getMessage(), null)
                        view.hideLoader()
                    }

                }catch (e: Exception) {
                    view.hideLoader()
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