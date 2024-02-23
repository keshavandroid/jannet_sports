package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.util.Log
import com.e.jannet_stable_code.retrofit.APIClient
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.UserServices
import com.e.jannet_stable_code.retrofit.bookinglistdata.BookTicketDetailResponse
import com.e.jannet_stable_code.retrofit.bookinglistdata.BookingListBaseResponse
import com.e.jannet_stable_code.retrofit.response.BasicResponse
import com.e.jannet_stable_code.utils.Utilities
import com.e.jannet_stable_code.viewinterface.IBookTicketDetailView
import com.e.jannet_stable_code.viewinterface.IBookTickitParentView
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class BookTicketDetailController (var context: Activity, internal var view: IBookTicketDetailView):
    IBookTicketDetailController {
    override fun callBookTicketDetailApi(id: String, token: String, event_id: String) {

        val apiInterface: UserServices = APIClient.getClient()!!.create(UserServices::class.java)
        val call: Call<ResponseBody?>? =
            apiInterface.bookTicketDetail(
                id,
                token,
                event_id
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

                    val response = gson.fromJson(reader, BookTicketDetailResponse::class.java)
                    if (response.getStatus() == 1) {
                        val data = response.getResult()
                        view.onBookTicketDetailSuccessful(data)
                        Utilities.showToast(context, response.getMessage())

                    } else Utilities.showToast(
                        context, response.getMessage()
                    )
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