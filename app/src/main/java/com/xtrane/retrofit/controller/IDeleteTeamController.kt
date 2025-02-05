package com.xtrane.retrofit.controller

import retrofit2.http.Query

interface IDeleteTeamController:IBaseController {

    fun callDeleteTeamApi(id:String,token:String,teamId:String)
}

