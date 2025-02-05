package com.xtrane.retrofit.controller

interface IPGetCoachListSportsTypeController :IBaseController {

    fun callCOachListApi(id:String,token:String,sportsType: String)
}