package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.response.GetProfileCoachApiResponse
import com.xtrane.retrofit.response.GetProfileParentApiResponse
import com.xtrane.utils.Constants
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.Reader
import java.io.StringReader
import java.lang.reflect.Modifier

private const val TAG = "GetProfileCont"
class GetProfileController(val context: Activity, val isParent:Boolean,internal val controllerInterface: ControllerInterface) {

    private  var id = ""
    private var token = ""
    private var userType = ""
    init {
        callApi()
    }

    private fun callApi() {
        Utilities.showProgress(context)
       val storedata = StoreUserData(context)

        if (storedata.getString(Constants.COACH_ID).trim()==null||storedata.getString(Constants.COACH_ID).trim().isEmpty()||storedata.getString(Constants.COACH_ID).trim()==""){


            id = SharedPrefUserData(context).getSavedData().id!!
            token = SharedPrefUserData(context).getSavedData().token!!
            userType = SharedPrefUserData(context).getSavedData().usertype!!

            Log.e(TAG, "callApi: profile controller cll parent====$id", )

        }else {


            id = storedata.getString(Constants.COACH_ID)
            token = storedata.getString(Constants.COACH_TOKEN)
            userType = "coach"
            Log.e(TAG, "callApi: profile controller cll coach====$id", )

        }

        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().getProfileApi(id,token,userType)

        RetrofitHelper.callApi(call, object : RetrofitHelper.ConnectionCallBack {
            override fun onSuccess(body: Response<ResponseBody?>?) {
                Utilities.dismissProgress()
                try {
                    val resp = body!!.body()!!.string()
                    Log.d(TAG, "onSuccess: success>>$resp<")
                    val reader: Reader = StringReader(resp)
                    val builder = GsonBuilder()
                    builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    val gson = builder.create()

                    if(isParent){
                        val response: GetProfileParentApiResponse = gson.fromJson(reader, GetProfileParentApiResponse::class.java)
                        Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                        if (response.getStatus() == 1) {
                            SharedPrefUserData(context).saveProfileResp(response.getResult()?.get(0))
                            controllerInterface.onSuccess(response, "LoginResp")
                        } else {
                            Log.d(TAG, "onSuccess: 0 status")
                            controllerInterface.onFail(response.getMessage())
                        }
                    }else{
                        val response: GetProfileCoachApiResponse = gson.fromJson(reader, GetProfileCoachApiResponse::class.java)
                        Log.d(TAG, "onSuccess: insuccess>>" + response.getStatus() + "<")
                        if (response.getStatus() == 1) {
                            SharedPrefUserData(context).saveProfileResp(response.getResult()?.get(0))
                            controllerInterface.onSuccess(response, "LoginResp")
                        } else {
                            Log.d(TAG, "onSuccess: 0 status")
                            controllerInterface.onFail(response.getMessage())
                        }
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
                //Utilities.showToast(context, "Server error")
                Log.d(TAG, "onError: ====")
                controllerInterface.onFail(error)
            }
        })
    }
}