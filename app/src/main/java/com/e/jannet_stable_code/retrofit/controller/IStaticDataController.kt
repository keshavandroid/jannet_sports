package com.e.jannet_stable_code.retrofit.controller

interface IStaticDataController:IBaseController {

    fun callStaticDataApi(pageId:String,id:String,token:String)
}

