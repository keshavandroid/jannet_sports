package com.xtrane.viewinterface

import com.xtrane.retrofit.teamdetaildata.TeamDetailResult

interface TeamDetailView:IBaseView {

    fun onTeamDetailSuccess(response:List<TeamDetailResult?>?)
}