package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.APIClient
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.UserServices
import com.xtrane.retrofit.parentbootomcoach.ParentBootomCoacheBaseResponse
import com.xtrane.retrofit.teamdetaildata.TeamDetailBaseResponse
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.IBokingListView
import com.xtrane.viewinterface.IParentBootomCoachesView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class ParentCoachesListController(var context: Activity, internal var view: IParentBootomCoachesView):IParentCoachesListController
   {
    override fun callCOachListApi(id: String, token: String) {
        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? = apiInterface.getCoachList(id, token)


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
                    val response: ParentBootomCoacheBaseResponse =
                        gson.fromJson(reader, ParentBootomCoacheBaseResponse::class.java)

                    if (response.getStatus() == 1) {
                        val data = response.getResult()!!
                        view.onBottomCoachesListSuccess(data)
                        Utilities.showToast(context, response.getMessage())

                    } else Utilities.showToast(context, response.getMessage())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun onError(code: Int, error: String?) {
                Log.d("TAG", "onError: ===" + error)
                //                Utils.showAlert((Activity) activity, error);
            }
        })
    }



    override fun onDestroy() {

    }

    override fun onFinish() {

    }
}