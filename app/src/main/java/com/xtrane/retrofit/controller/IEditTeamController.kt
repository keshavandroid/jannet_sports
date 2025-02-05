package com.xtrane.retrofit.controller

interface IEditTeamController:IBaseController {

    fun callEditTeamApi(id:String,token:String,teamId:String,teamName:String,description:String,image:String)
}

