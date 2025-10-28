package com.xtrane.retrofit.controller

interface IRosterFillingController:IBaseController {

    fun CallEvent(id:String,token:String,eventID:String,coachId:String,date:String,time:String)

}