package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.e.jannet_stable_code.retrofit.APIClient
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.UserServices
import com.e.jannet_stable_code.retrofit.deleteteam.DeleteTeam
import com.e.jannet_stable_code.viewinterface.IDeleteMacthView
import com.e.jannet_stable_code.viewinterface.IDeleteTeamView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class DeleteMatchCOntroller(var context: Activity, internal var view: IDeleteMacthView):IDeleteMatchController {
    override fun callDeleteMatchApi(id: String, token: String, matchId: String) {
        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.deleteMatch(id,token,matchId)


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
                        view.onDeleteMatchSuccessfull()

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