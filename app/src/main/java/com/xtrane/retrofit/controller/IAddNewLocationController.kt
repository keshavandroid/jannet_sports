package com.xtrane.retrofit.controller

interface IAddNewLocationController:IBaseController {

    fun callAddNewLocationApi(id:String,token:String,address:String,latitude:String,longitude:String,
                              coat:String)


}