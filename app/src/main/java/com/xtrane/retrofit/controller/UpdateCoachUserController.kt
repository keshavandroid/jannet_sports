package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.CoachRegisterResponse
import com.xtrane.ui.loginRegister.registerCoachScreen.RegisterCoachActivity
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

class UpdateCoachUserController(
    var context: Activity,
    internal var controllerInterface: ControllerInterface
) {
    private val TAG = "RegisUserCont"
    fun callApi(
        imageStr: String,
        fname: String,
        email: String,
        phNo: String,
        bdate: String,
        gender: String,
        location: String,
        sportsIds: String
    ) {
        Utilities.showProgress(context)

        val storData = StoreUserData(context)
        val idd  = storData.getString(Constants.COACH_ID)
        val tokenn = storData.getString(Constants.COACH_TOKEN)
        val id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull()!!,
            idd
        )
        val token: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull()!!,
            tokenn
        )
        val fname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, fname)
        val email: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, email)
        val phNo: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, phNo)
        val bdate: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, bdate)
        val gender: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, gender)
        val location: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, location)
        val sportsIds: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, sportsIds)

        var image: MultipartBody.Part? = null
        val file_path: String = imageStr
        if (file_path.isNotEmpty()) {
            val file = File(file_path)
            val reqFile = RequestBody.create(
                context.contentResolver.getType(
                    Utilities.getImageContentUri(
                        context,
                        file
                    )!!
                )!!.toMediaTypeOrNull(), file
            )
            image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().editCoachProfileApi(
            id = id,
            token = token,
            firstname = fname,
            sports = sportsIds,
            gender = gender,
            birthdate = bdate,
            email = email,
            contactNo = phNo,
            location = location,
            image = image
        )

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: insuccess>>$resp<")
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
                    Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
                        SharedPrefUserData(context).saveProfileResp(response)
                        controllerInterface.onSuccess(response, "RegisterResp")
                    } else {
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