package com.xtrane.retrofit.controller

interface IDeviceRegisterController:IBaseController {

    fun callDeviceRegister(regId:String,user_id:String,device_id:String,email:String)

}