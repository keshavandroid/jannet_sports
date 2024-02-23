package com.e.jannet_stable_code.retrofit.controller

interface ICoachFilterController:IBaseController {

    fun callCoachFilterApi(id:String,token:String,location_id:String,sportsId:String)
}