package com.xtrane.retrofit.controller

interface ICoachTeamListController:IBaseController {

    fun callCoachTeamListApi(id:String,token:String,coach_id:String)
}