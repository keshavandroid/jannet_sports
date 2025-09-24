package com.xtrane.retrofit.controller

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.gradlistdata.GralistBaseResponse
import com.xtrane.viewinterface.IGetGradeListView
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.nonparticipantdata.GetMemberResult
import com.xtrane.retrofit.nonparticipantdata.NonParticipantBaseResponse
import com.xtrane.viewinterface.IGetTeamMemberView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class GetTeamMemberController(var context: Activity, internal var view: IGetTeamMemberView):IGetTeamMemberController {

    override fun callITeamMember(id: String, token: String, event_id: String) {

        view.showLoader()


        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.getMemberList(id,token,event_id)


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

                    val response = gson.fromJson(reader, GetMemberResult::class.java)

                    if (response.getStatus() == 1) {
                        val data = response.getResult()
                        view.onGetTeamMember(data)

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
      //  TODO("Not yet implemented")
    }

    override fun onFinish() {
        //TODO("Not yet implemented")
    }
}