package com.xtrane.viewinterface

import com.xtrane.retrofit.teamlistdata.TeamListResult

interface ITeamListView:IBaseView {

fun onTeamListSuccess(response:List<TeamListResult?>?)
}