package com.xtrane.retrofit.controller

interface IAcceptRejectEventController:IBaseController {

    fun acceptRejectEvent(id:String,token:String,childId: String,eventId: String,status:String)

    fun acceptRejectTicket(id:String,token:String,childId: String,eventId: String,status:String,matchId: String)


}