package com.xtrane.retrofit.controller

import android.app.Activity
import com.xtrane.ui.parentsApp.RegisterData
import com.xtrane.utils.SharedPrefUserData
import com.xtrane.viewinterface.RegisterControllerInterface
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


class AdMatchPriceController(
    var context: Activity,
    internal var controllerInterface: RegisterControllerInterface) {

    var storeusedata = SharedPrefUserData(context)

    fun addMatchPrice(registerData: RegisterData) {



        var userId: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),
            storeusedata.getSavedData().id!!
        )
        var user_token: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),
            storeusedata.getSavedData().token!!
        )
        var match_id: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerData.matchId1)
        var match_price: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerData.matchPrice1)
        var counter: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), registerData.counter
        )


    }


}