package com.xtrane.retrofit.controller

interface ICoachNumberSelectionController:IBaseController {

    fun callCoachNumberListApi(id:String,token:String,eventid:String,coach_id:String)
}