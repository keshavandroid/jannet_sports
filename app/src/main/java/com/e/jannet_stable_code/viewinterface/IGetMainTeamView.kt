package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.MainTeamListdata.MainTeamListResult

interface IGetMainTeamView:IBaseView  {


    fun onMainTeamListSuccess(response:List<MainTeamListResult?>?)
}