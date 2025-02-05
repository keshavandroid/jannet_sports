package com.xtrane.retrofit.controller

interface IBookTicketDetailController :IBaseController {

    fun callBookTicketDetailApi(id:String,token:String,event_id:String)
}