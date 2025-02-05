package com.xtrane.retrofit.controller

import java.util.*

interface INonParticipantController:IBaseController {

    fun callNonParticipantApi(id:String,token:String,event_id:String)
}
