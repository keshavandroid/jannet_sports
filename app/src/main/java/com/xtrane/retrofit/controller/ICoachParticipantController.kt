package com.xtrane.retrofit.controller

import java.util.*

interface ICoachParticipantController:IBaseController {

    fun callNonParticipantApi(id:String,token:String,event_id:String)
}
