package com.e.jannet_stable_code.retrofit.controller

interface IProfileController:IBaseController {

    fun callGetProfileAPI(id:String,token:String,userType:String)
}