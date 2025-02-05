package com.xtrane.viewinterface

import com.xtrane.retrofit.MainTeamListdata.MainTeamListResult

interface IGetMainTeamView:IBaseView  {


    fun onMainTeamListSuccess(response:List<MainTeamListResult?>?)
}