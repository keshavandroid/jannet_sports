package com.e.jannet_stable_code.retrofit.controller

interface ICoachEventListController:IBaseController {

    fun callCoachEventListApi(id:String,token:String,coach_id:String)
}