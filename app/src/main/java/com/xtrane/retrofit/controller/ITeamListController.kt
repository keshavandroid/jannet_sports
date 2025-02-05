package com.xtrane.retrofit.controller

interface ITeamListController:IBaseController {

    fun callTeamLostApi(id:String,token:String,event_id:String)
}