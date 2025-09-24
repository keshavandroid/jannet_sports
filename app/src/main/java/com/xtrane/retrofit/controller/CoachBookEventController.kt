package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.BasicResponse
import com.xtrane.ui.loginRegister.addChildScreen.AddChildActivity
import com.xtrane.utils.SharedPrefUserData
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

class CoachBookEventController(var context: Activity, internal var controllerInterface: ControllerInterface) {
    private val TAG = "CoachBookEventCont"

    fun callApi(feesStr:String,eventIdStr:String,imageStr:String,idd:String,tokenn:String) {

        Log.e("callApi=imageStr==",imageStr)

        Utilities.showProgress(context)

        val id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,idd)
        val token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, tokenn)
        val fees: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, feesStr)
        val event_id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, eventIdStr)
        val bookPaymentType: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, "stripe")

        var image: MultipartBody.Part? = null
        val file_path: String = imageStr

        if (file_path.isNotEmpty()) {
            val file = File(file_path)
            val imageUri = Utilities.getImageContentUri(context, file)
            if (imageUri != null) {
                val mimeType = context.contentResolver.getType(imageUri)
                if (mimeType != null) {
                    val reqFile = RequestBody.create(mimeType.toMediaTypeOrNull(), file)
                    image = MultipartBody.Part.createFormData("image", file.name, reqFile)
                }
            }
        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().coachBookEventApi(id,token,event_id,fees,bookPaymentType,image)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: insuccess>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: BasicResponse = gson.fromJson(reader, BasicResponse::class.java)
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")

                    if (response.getStatus() == 1) {
                        controllerInterface.onSuccess(response, "AddChildSuccess")
                    }
                    else {
                        Log.d(TAG, "onSuccess: 0 status")
                        Utilities.showToast(context, response.getMessage())
                        controllerInterface.onFail(response.getMessage())
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: insuccess>>" + e.message + "<")
                    Utilities.showToast(context, "Something went wrong.")
                    e.printStackTrace()
                    controllerInterface.onFail(e.message)
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Utilities.showToast(context, "Server error")
                Log.d(TAG, "onError: ====")
                controllerInterface.onFail(error)
            }
        })
    }
}