package com.xtrane.retrofit.controller

interface IBookingListController:IBaseController  {

    fun callBookingDetailApi(id:String,token:String,event_id:String)
}
