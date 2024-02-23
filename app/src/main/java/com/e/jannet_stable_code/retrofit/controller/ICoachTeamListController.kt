package com.e.jannet_stable_code.retrofit.controller

interface ICoachTeamListController:IBaseController {

    fun callCoachTeamListApi(id:String,token:String,coach_id:String)
}