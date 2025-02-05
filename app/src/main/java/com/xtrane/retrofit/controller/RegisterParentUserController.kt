package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.RegisterParentResponse
import com.xtrane.ui.loginRegister.RegisterAdultActivity
import com.xtrane.ui.loginRegister.RegisterParentActivity
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

class RegisterParentUserController(var context: Activity, internal var controllerInterface: ControllerInterface) {
    private val TAG = "RegisUserCont"
    fun callApi(registerObj: RegisterParentActivity.ParentRegisterObject) {
        Utilities.showProgress(context)

        val email: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.email)
        val password: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.password)
        val fname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.firstname)
        val mname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.middlename)

        val lname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.lastname)
        val contactNo: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.contactNo)
        val gender: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.gender)
        val bdate: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.birthdate)
        val userType: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.userType)

        var image: MultipartBody.Part? = null
        val file_path: String = registerObj.image
        if (file_path.isNotEmpty()) {
            val file = File(file_path)
            val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
            image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().registerParentApi(email,password,fname,mname,lname,contactNo,gender,bdate,userType,image)

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
                    val response: RegisterParentResponse = gson.fromJson(reader, RegisterParentResponse::class.java)
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

    fun callApiAdult(registerObj: RegisterAdultActivity.ParentRegisterObject) {
        Utilities.showProgress(context)

        val email: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.email)
        val password: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.password)
        val fname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.firstname)
        val mname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.middlename)

        val lname: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.lastname)
        val contactNo: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.contactNo)
        val gender: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.gender)
        val bdate: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.birthdate)
        val userType: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.userType)

        var image: MultipartBody.Part? = null
        val file_path: String = registerObj.image
        if (file_path.isNotEmpty()) {
            val file = File(file_path)
            val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
            image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().registerParentApi(email,password,fname,mname,lname,contactNo,gender,bdate,userType,image)

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
                    val response: RegisterParentResponse = gson.fromJson(reader, RegisterParentResponse::class.java)
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