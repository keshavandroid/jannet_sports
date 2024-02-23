package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.util.Log
import com.e.jannet_stable_code.retrofit.APIClient
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.UserServices
import com.e.jannet_stable_code.retrofit.parentbootomcoach.ParentBootomCoacheBaseResponse
import com.e.jannet_stable_code.retrofit.response.BasicResponse
import com.e.jannet_stable_code.utils.Utilities
import com.e.jannet_stable_code.viewinterface.IBookTickitParentView
import com.e.jannet_stable_code.viewinterface.IParentBootomCoachesView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class BookTicketParentController(var context: Activity, internal var view: IBookTickitParentView) :
    IBookTicketParentController {
    override fun callBookTicketApi(
        id: String,
        token: String,
        event_id: String,
        fees: String,
        total_ticket: String,
        match_id: String,
        name: String,
        contactNo: String
    ) {

        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? =
            apiInterface.bookTicketParent(
                id,
                token,
                event_id,
                fees,
                total_ticket,
                match_id,
                name,
                contactNo
            )

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
                    val response: BasicResponse =
                        gson.fromJson(reader, BasicResponse::class.java)

                    if (response.getStatus() == 1) {
                        view.onBookTicketSuccessful(response.getMessage()!!)
                        Utilities.showToast(context, response.getMessage())

                    } else{
                        view.onBookTicketFailed()
                        Utilities.showToast(context, response.getMessage())
                    }
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
        TODO("Not yet implemented")
    }

    override fun onFinish() {
        TODO("Not yet implemented")
    }
}