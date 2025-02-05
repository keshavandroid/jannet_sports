package com.xtrane.retrofit.controller

interface IGetTicketsCOntroller:IBaseController {

    fun callGetTicketsApi(id:String,token:String,event_id:String)

}