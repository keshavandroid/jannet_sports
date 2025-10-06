package com.xtrane.retrofit.controller

interface IGetTeamMemberController:IBaseController {

    fun callITeamMember(id: String, token: String, eventid: String, coach_id: String?)
}

