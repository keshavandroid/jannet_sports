package com.e.jannet_stable_code.retrofit.controller

import android.app.Activity
import android.util.Log
import com.e.jannet_stable_code.retrofit.RetrofitHelper
import com.e.jannet_stable_code.retrofit.mainteamdata.AddMainTeamBaseResponse
import com.e.jannet_stable_code.retrofit.response.BasicResponse
import com.e.jannet_stable_code.retrofit.response.CoachRegisterResponse
import com.e.jannet_stable_code.ui.parentsApp.RegisterData
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.utils.Utilities
import com.e.jannet_stable_code.viewinterface.RegisterControllerInterface
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

class AddMainTeamController(var context: Activity, var registerControllerInterface: RegisterControllerInterface) {

    fun addTeam(registerData: RegisterData) {

        val storedata = StoreUserData(context)
        val idd = storedata.getString(Constants.COACH_ID)
        val tokenn = storedata.getString(Constants.COACH_TOKEN)
//        SharedPrefUserData(context).getSavedData().id
        val id: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, idd)
        val token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!,tokenn)

        var coach_id: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.coach_id)
        var teamName: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.teamName)
        var description: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, registerData.description)


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
            .addMainTeamApi(id,token,coach_id,teamName,description,image)

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
                    val response: AddMainTeamBaseResponse =
                        gson.fromJson(reader, AddMainTeamBaseResponse::class.java)
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