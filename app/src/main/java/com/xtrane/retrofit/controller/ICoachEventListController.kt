package com.xtrane.retrofit.controller

interface ICoachEventListController:IBaseController {

    fun callCoachEventListApi(id:String,token:String,coach_id:String,type:String)
}