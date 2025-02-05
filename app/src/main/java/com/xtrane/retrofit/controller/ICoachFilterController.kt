package com.xtrane.retrofit.controller

interface ICoachFilterController:IBaseController {

    fun callCoachFilterApi(id:String,token:String,location_id:String,sportsId:String)
}