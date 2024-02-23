package com.e.jannet_stable_code.retrofit.controller

interface IDeviceRegisterController:IBaseController {

    fun callDeviceRegister(regId:String,user_id:String,device_id:String,email:String)

}