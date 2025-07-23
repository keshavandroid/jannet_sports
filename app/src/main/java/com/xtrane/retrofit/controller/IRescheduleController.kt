package com.xtrane.retrofit.controller

interface IRescheduleController:IBaseController {

    fun CallRescheduleEvent(id:String,token:String,eventID:String,date:String,time:String)

}