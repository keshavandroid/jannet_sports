package com.xtrane.retrofit.controller

interface IBookingDetailController:IBaseController {

    fun callBookingDetailApi(id:String,token:String,book_id:String)
}