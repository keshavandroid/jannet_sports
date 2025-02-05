package com.xtrane.retrofit.controller

import retrofit2.http.Query

interface IDeleteEventController:IBaseController {


    fun callDeleteEventApi(id:String,token:String,eventId:String)

}