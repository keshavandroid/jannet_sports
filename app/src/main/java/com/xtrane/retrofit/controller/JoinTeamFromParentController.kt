package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.mainteamdata.AddMainTeamBaseResponse
import com.xtrane.retrofit.response.BasicResponse
import com.xtrane.ui.parentsApp.RegisterData
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
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
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

class JoinTeamFromParentController(var context: Activity, var registerControllerInterface: RegisterControllerInterface) {

    fun joinTeam(registerData: RegisterData) {

        val storedata = SharedPrefUserData(context)
        val idd = storedata.getSavedData().id
        val tokenn = storedata.getSavedData().token
        val id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, idd)
        val token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,tokenn)

        var coach_id: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.coach_id)
        var team_id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.teamId)
        var fees: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.fees)


        var image: MultipartBody.Part? = null
        val file_path: String = registerData.image!!
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

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI()
            .joinTeamFromParent(id,token,coach_id,team_id,fees,image)

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
                    val response: BasicResponse =
                        gson.fromJson(reader, BasicResponse::class.java)
                    Log.d("TAG", "onSuccess: insuccess>>" + response.getStatus() + "<")
                    if (response.getStatus() == 1) {
//                        SharedPrefUserData(context).saveRegisterCoach(response.getResult())
//                        SharedPrefUserData(context).setRegisterStep("1")
                        registerControllerInterface.onSuccess(response)
                    } else {
                        Log.d("TAG", "onSuccess: 0 status")
                        Utilities.showToast(context, response.getMessage())
                        registerControllerInterface.onFail(response.getMessage())
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "onSuccess: insuccess>>" + e.message + "<")
                    Utilities.showToast(context, "Something went wrong.")
                    e.printStackTrace()
                    registerControllerInterface.onFail(e.message)
                }
            }


            override fun onError(code: Int, error: String?) {
                Utilities.dismissProgress()
                Utilities.showToast(context, "Server error")
                Log.d("TAG", "onError: ====")
                registerControllerInterface.onFail(error)
            }
        })


    }


}