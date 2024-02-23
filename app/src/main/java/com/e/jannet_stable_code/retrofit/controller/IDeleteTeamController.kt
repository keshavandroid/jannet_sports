package com.e.jannet_stable_code.retrofit.controller

import retrofit2.http.Query

interface IDeleteTeamController:IBaseController {

    fun callDeleteTeamApi(id:String,token:String,teamId:String)
}

