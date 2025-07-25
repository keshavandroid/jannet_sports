package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.BasicResponse
import com.xtrane.ui.parentsApp.RegisterData
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.Utilities
import com.xtrane.viewinterface.RegisterControllerInterface
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class BookChildEventController(var context: Activity, var registerControllerInterface: RegisterControllerInterface) {

    fun bookEvent(registerData: RegisterData){

        val id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, SharedPrefUserData(context).getSavedData().id!!)
        val token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, SharedPrefUserData(context).getSavedData().token!!)
        var child_id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.child_id)
        var fees: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.fees)
        var event_id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.event_id)
        var parentId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,SharedPrefUserData(context).getSavedData().id!!)


        var part: MultipartBody.Part? = null
        val file_path: String = registerData.image!!
        if (file_path.isNotEmpty()) {
            val file = File(file_path)
            val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
            part = MultipartBody.Part.createFormData("image", file.name, reqFile)
        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().bookChildEvent(id,token,child_id,fees,event_id,parentId,part)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: BasicResponse = gson.fromJson(reader, BasicResponse::class.java)

                    if (response.getStatus()== 1) {
                        val data = response.getMessage()
//
                        Utilities.showToast(context, response.getMessage())
                        registerControllerInterface.onSuccess(data)
                    } else Utilities.showToast(context, response.getMessage())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                registerControllerInterface.onFail(error)
                Log.d("TAG", "onError: ===" + error)
                //                Utils.showAlert((Activity) activity, error);
            }
        })



    }

    fun bookRequestEvent(id:String,token:String,child_id:String,event_id:String){
        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().bookEventRequest(id,token,child_id,event_id)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()
                    val response: BasicResponse = gson.fromJson(reader, BasicResponse::class.java)

                    if (response.getStatus()== 1) {
                        val data = response.getMessage()
//
                        Utilities.showToast(context, response.getMessage())
                        registerControllerInterface.onSuccess(data)
                    } else Utilities.showToast(context, response.getMessage())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                registerControllerInterface.onFail(error)
                Log.d("TAG", "onError: ===" + error)
                //                Utils.showAlert((Activity) activity, error);
            }
        })
    }
}