package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.CoachRegisterResponse
import com.xtrane.ui.parentsApp.ParentProfileObject
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

class EditProfileParentController(
    var context: Activity,
    internal var controllerInterface: ControllerInterface
) {
    private val TAG = "EditProfileParentCont"
    fun callApi(registerObj: ParentProfileObject) {
        Utilities.showProgress(context)

        val id: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull()!!,
            SharedPrefUserData(context).getSavedData().id!!
        )
        val token: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull()!!,
            SharedPrefUserData(context).getSavedData().token!!
        )
        val name: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.firstname)
        val password: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.password)
        val gender: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.gender)
        val birthdate: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.birthdate)
        val email: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.email)
        val contactno: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.phoneNumber)
        val usertype: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.type)
        val firstname: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.firstname)
        val lastname: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.lastname)

        val middlename: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.middlename)

        var image: MultipartBody.Part? = null
        val file_path: String = registerObj.image!!
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

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().editParentProfileApi(
            id = id,
            token = token,
            name = name,
            password = password,
            gender = gender,
            birthdate = birthdate,
            email = email,
            contactNo = contactno,
            usertype = usertype,
            firstname = firstname,
            lastname = lastname,
            middleName=middlename,
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
                        SharedPrefUserData(context).saveRegisterCoach(response.getResult())
                        SharedPrefUserData(context).setRegisterStep("1")
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