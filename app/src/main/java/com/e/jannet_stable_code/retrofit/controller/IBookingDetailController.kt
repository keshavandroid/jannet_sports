package com.e.jannet_stable_code.retrofit.controller

interface IBookingDetailController:IBaseController {

    fun callBookingDetailApi(id:String,token:String,book_id:String)
}