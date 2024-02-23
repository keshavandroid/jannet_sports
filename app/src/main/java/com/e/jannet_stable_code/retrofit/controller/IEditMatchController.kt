package com.e.jannet_stable_code.retrofit.controller

interface IEditMatchController:IBaseController {

    fun callEditMatchApi(id:String,token:String,match_id:String,team_a_id:String,team_b_id:String,coat:String,time:String)
}

