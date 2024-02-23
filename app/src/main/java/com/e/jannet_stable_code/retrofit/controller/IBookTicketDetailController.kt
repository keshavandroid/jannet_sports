package com.e.jannet_stable_code.retrofit.controller

interface IBookTicketDetailController :IBaseController {

    fun callBookTicketDetailApi(id:String,token:String,event_id:String)
}