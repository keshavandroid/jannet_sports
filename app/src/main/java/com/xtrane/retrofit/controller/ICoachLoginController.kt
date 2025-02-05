package com.xtrane.retrofit.controller

interface ICoachLoginController:IBaseController {

    fun callCoachLoginApi(email:String,password:String)

}