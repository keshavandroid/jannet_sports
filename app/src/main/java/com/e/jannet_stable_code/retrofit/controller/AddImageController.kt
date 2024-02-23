package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.util.Log
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.response.BasicResponse
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.EditEventActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities
import com.google.gson.GsonBuilder
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

class AddImageController(var context: Activity, internal var controllerInterface: ControllerInterface) {

    fun callApi(obj: EditEventActivity.AddImageObject) {

        Utilities.showProgress(context)
        val storedata = StoreUserData(context)
        val idc = storedata.getString(Constants.COACH_ID)
        val tokenc = storedata.getString(Constants.COACH_TOKEN)

        val id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull()!!, idc
        )
        val token: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull()!!,
            tokenc
        )

        val event_ID: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.event_id)

        val imgCount: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imgCount)

        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        var image4: MultipartBody.Part? = null
        var image5: MultipartBody.Part? = null
        var image6: MultipartBody.Part? = null



        if (obj.img2.isNotEmpty()) {
            val file = File(obj.img2)
            val reqFile = RequestBody.create(
                context.contentResolver.getType(
                    Utilities.getImageContentUri(
                        context,
                        file
                    )!!
                )!!.toMediaTypeOrNull(), file
            )
            image2 = MultipartBody.Part.createFormData("image1", file.name, reqFile)
        }
        if (obj.img3.isNotEmpty()) {
            val file = File(obj.img3)
            val reqFile = RequestBody.create(
                context.contentResolver.getType(
                    Utilities.getImageContentUri(
                        context,
                        file
                    )!!
                )!!.toMediaTypeOrNull(), file
            )
            image3 = MultipartBody.Part.createFormData("image2", file.name, reqFile)
        }
        if (obj.img4.isNotEmpty()) {
            val file = File(obj.img4)
            val reqFile = RequestBody.create(
                context.contentResolver.getType(
                    Utilities.getImageContentUri(
                        context,
                        file
                    )!!
                )!!.toMediaTypeOrNull(), file
            )
            image4 = MultipartBody.Part.createFormData("image3", file.name, reqFile)
        }
        if (obj.img5.isNotEmpty()) {
            val file = File(obj.img5)
            val reqFile = RequestBody.create(
                context.contentResolver.getType(
                    Utilities.getImageContentUri(
                        context,
                        file
                    )!!
                )!!.toMediaTypeOrNull(), file
            )
            image5 = MultipartBody.Part.createFormData("image4", file.name, reqFile)
        }
        if (obj.img6.isNotEmpty()) {
            val file = File(obj.img6)
            val reqFile = RequestBody.create(
                context.contentResolver.getType(
                    Utilities.getImageContentUri(
                        context,
                        file
                    )!!
                )!!.toMediaTypeOrNull(), file
            )
            image6 = MultipartBody.Part.createFormData("image5", file.name, reqFile)
        }



        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().addImage(
            id = id,
            token = token,
            event_id = event_ID,
            count = imgCount,
            image1 = image2,
            image2 = image3,
            image3 = image4,
            image4 = image5,
            image5 = image6
        )

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
                    val response: BasicResponse = gson.fromJson(reader, BasicResponse::class.java)
                    Log.d("TAG", "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
                        controllerInterface.onSuccess(response, "AddChildSuccess")
                    } else {
                        Log.d("TAG", "onSuccess: 0 status")
                        Utilities.showToast(context, response.getMessage())
                        controllerInterface.onFail(response.getMessage())
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "onSuccess: insuccess>>" + e.message + "<")
                    Utilities.showToast(context, "Something went wrong.")
                    e.printStackTrace()
                    controllerInterface.onFail(e.message)
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Utilities.showToast(context, "Server error")
                Log.d("TAG", "onError: ====")
                controllerInterface.onFail(error)
            }
        })

    }
}