package com.xtrane.retrofit.controller

import android.app.Activity
import android.util.Log
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.ImageListObject
import com.xtrane.retrofit.RetrofitHelper
import com.xtrane.retrofit.addeventBaseResponse.AddEventBaseResponse
import com.xtrane.retrofit.response.BasicResponse
import com.xtrane.ui.coachApp.addEventScreen.AddEventActivity
import com.xtrane.ui.coachApp.addEventScreen.EditEventActivity
import com.xtrane.utils.Constants
import com.xtrane.utils.StoreUserData
import com.xtrane.utils.Utilities
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

class EditEventController(
    var context: Activity,
    internal var controllerInterface: ControllerInterface
) {

    private val TAG = "AddEventCoachCont"

    fun callApi(obj: EditEventActivity.EditEventObject) {

        val imgList=ArrayList<String>()
        val imgId=ArrayList<String>()
        val finalImages=ArrayList<ImageListObject>()

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

        val event_name: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.event_name)

        val description: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.description)

        val fees: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.fees)
        val location: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.location)
        val sportdId: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.sportTypes)
        val coachId: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, idc)


        val date: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.date)
        val participants: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.noParticipants)

        val gender_applicable: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.gender_applicable)
        val grade_id: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.grade_id)
        val min_age: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.minimum_age)
        val max_age: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.max_age)
        val imgCount: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imgCount)

        val imgId1: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imageId1)
        val imgId2: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imageId2)
        val imgId3: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imageId3)
        val imgId4: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imageId4)
        val imgId5: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imageId5)

        val imgId6: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.imageId6)

        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        var image4: MultipartBody.Part? = null
        var image5: MultipartBody.Part? = null
        var image6: MultipartBody.Part? = null


        if (obj.img1.isNotEmpty()) {
            val file = File(obj.img1)
            val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
            image1 = MultipartBody.Part.createFormData("mainimage", file.name, reqFile)
        }


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


//        if(obj.img2.isNotEmpty()){
//            val imgId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.image_id)
//
//            var image: MultipartBody.Part? = null
//            val file_path: String = obj.img2
//            if (file_path.isNotEmpty()) {
//                val file = File(file_path)
//                val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
//                image = MultipartBody.Part.createFormData("image1", file.name, reqFile)
//            }
//            val item=ImageListObject(imgId,image)
//            finalImages.add(item)
//        }
//        if (obj.img3.isNotEmpty()) {
//            val imgId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.image_id)
//            var image: MultipartBody.Part? = null
//            val file_path: String = obj.img3
//            if (file_path.isNotEmpty()) {
//                val file = File(file_path)
//                val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
//                image = MultipartBody.Part.createFormData("image2", file.name, reqFile)
//            }
//            val item=ImageListObject(imgId,image)
//            finalImages.add(item)
//        }
//        if (obj.img4.isNotEmpty()) {
//            val imgId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.image_id)
//            var image: MultipartBody.Part? = null
//            val file_path: String = obj.img4
//            if (file_path.isNotEmpty()) {
//                val file = File(file_path)
//                val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
//                image = MultipartBody.Part.createFormData("image3", file.name, reqFile)
//            }
//            val item=ImageListObject(imgId,image)
//            finalImages.add(item)
//        }
//        if (obj.img5.isNotEmpty()) {
//            val imgId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.image_id)
//            var image: MultipartBody.Part? = null
//            val file_path: String = obj.img5
//            if (file_path.isNotEmpty()) {
//                val file = File(file_path)
//                val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
//                image = MultipartBody.Part.createFormData("image4", file.name, reqFile)
//            }
//            val item=ImageListObject(imgId,image)
//            finalImages.add(item)
//        }
//        if (obj.img6.isNotEmpty()) {
//            val imgId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull()!!, obj.image_id)
//            var image: MultipartBody.Part? = null
//            val file_path: String = obj.img5
//            if (file_path.isNotEmpty()) {
//                val file = File(file_path)
//                val reqFile = RequestBody.create(context.contentResolver.getType(Utilities.getImageContentUri(context, file)!!)!!.toMediaTypeOrNull(), file)
//                image = MultipartBody.Part.createFormData("image5", file.name, reqFile)
//            }
//            val item=ImageListObject(imgId,image)
//            finalImages.add(item)
//        }
//
//        finalImages.toList()

        Log.e(TAG, "callApi: finalimages list $finalImages", )


        val call: Call<ResponseBody?>? = RetrofitHelper.getAPI().editEventApi(
            id = id,
            token = token,
            event_id = event_ID,
            event_name=event_name,
            description = description,
            fees = fees,
            location_id = location,
            sports_id = sportdId,
            coach_id = coachId,
            date = date,
            participants = participants,
            gender_applicable = gender_applicable,
            grade_id = grade_id,
            min_age = min_age,
            max_age = max_age,
            imageCount = imgCount,
            imageId1 = imgId1,
            imageId2 = imgId2,
            imageId3 = imgId3,
            imageId4= imgId4,
            imageId5 = imgId5,
            imageId6 = imgId6,
            mainimage = image1,
            image1 = image2,
            image2 = image3,
            image3 = image4,
            image4 = image5,
            image5 = image6
//            images = finalImages
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