package com.xtrane.retrofit.controller

interface IBookTicketParentController:IBaseController {

    fun callBookTicketApi(id:String,token:String,event_id:String,fees:String,total_ticket:String,match_id: String, name: String, contactNo: String,bookPaymentType:String)
}

