package com.xtrane.retrofit.controller

interface IRegisterEventRequestController:IBaseController {

    fun callEventRegisterRequestApi(id:String,token:String,status:String)

}