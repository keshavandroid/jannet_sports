package com.xtrane.retrofit.controller

interface IStaticDataController:IBaseController {

    fun callStaticDataApi(pageId:String,id:String,token:String)
}

