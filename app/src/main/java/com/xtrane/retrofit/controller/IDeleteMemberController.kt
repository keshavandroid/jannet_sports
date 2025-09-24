package com.xtrane.retrofit.controller

interface IDeleteMemberController:IBaseController {

    fun callDeleteMemberApi(id:String,token:String,memberId:String,eventId:String,teamId:String)
}

