package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.teamlistdata.TeamListResult

interface ITeamListView:IBaseView {

fun onTeamListSuccess(response:List<TeamListResult?>?)
}