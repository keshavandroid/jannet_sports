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

class AddChildToParentController(var context: Activity, internal var controllerInterface: ControllerInterface) {
    private val TAG = "AddChiToParCont"
    fun callApi(registerObj: AddChildActivity.ChildObject) {
        Utilities.showProgress(context)

        val allowBook: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, "1")

        val id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, SharedPrefUserData(context).getSavedData().id)
        val token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, SharedPrefUserData(context).getSavedData().token)
        val password: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.password)
        val childName: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.firstName)
        val middleName:RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.middleName)
        val lastName:RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.lastName)
        val jurseyName:RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.jurseyName)
        val gradeId:RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.gradeId)
        val email: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.email)
//        val password: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.password)
        val birthdate: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.birthdate)
        val sportsIds: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.sportsIds)
        val childGender: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerObj.childGender)

        var image: MultipartBody.Part? = null
        val file_path: String = registerObj.childImage
        if (file_path.isNotEmpty()) {
            val file = File(file_path)
            val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
            image = MultipartBody.Part.createFormData("childImage", file.name, reqFile)
        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().registerChildUser(allowBook,id,token,password,childName,middleName,lastName,jurseyName,email,birthdate,sportsIds,gradeId,childGender,image)

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