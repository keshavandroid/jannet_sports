package com.e.jannet_stable_code.retrofit.controller

import retrofit2.http.Query

interface IDeleteEventController:IBaseController {


    fun callDeleteEventApi(id:String,token:String,eventId:String)

}