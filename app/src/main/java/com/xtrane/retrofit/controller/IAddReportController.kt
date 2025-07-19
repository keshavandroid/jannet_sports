package com.xtrane.retrofit.controller

interface IAddReportController:IBaseController {

    fun CallAddNewReport(id:String,token:String,address:String,message:String)

}