package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.teamdetaildata.TeamDetailResult

interface TeamDetailView:IBaseView {

    fun onTeamDetailSuccess(response:List<TeamDetailResult?>?)
}