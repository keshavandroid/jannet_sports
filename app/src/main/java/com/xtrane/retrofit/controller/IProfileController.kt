package com.xtrane.retrofit.controller

interface IProfileController:IBaseController {

    fun callGetProfileAPI(id:String,token:String,userType:String)
}