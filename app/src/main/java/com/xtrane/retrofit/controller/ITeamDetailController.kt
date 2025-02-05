package com.xtrane.retrofit.controller

interface ITeamDetailController:IBaseController {

 fun callEventDetailApi(id:String,token:String,team_id:String,event_id:String)
}

