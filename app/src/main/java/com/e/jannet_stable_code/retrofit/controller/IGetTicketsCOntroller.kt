package com.e.jannet_stable_code.retrofit.controller

interface IGetTicketsCOntroller:IBaseController {

    fun callGetTicketsApi(id:String,token:String,event_id:String)

}