package com.e.jannet_stable_code.retrofit.controller

interface IBookingListController:IBaseController  {

    fun callBookingDetailApi(id:String,token:String,event_id:String)
}
