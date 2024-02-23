package com.e.jannet_stable_code.retrofit.controller

interface IPGetCoachListSportsTypeController :IBaseController {

    fun callCOachListApi(id:String,token:String,sportsType: String)
}