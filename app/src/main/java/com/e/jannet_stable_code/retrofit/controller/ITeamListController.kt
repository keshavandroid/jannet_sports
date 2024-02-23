package com.e.jannet_stable_code.retrofit.controller

interface ITeamListController:IBaseController {

    fun callTeamLostApi(id:String,token:String,event_id:String)
}