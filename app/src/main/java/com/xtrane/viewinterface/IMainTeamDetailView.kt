package com.xtrane.viewinterface

import com.xtrane.retrofit.MainTeamListdata.MainTeamListResult
import com.xtrane.retrofit.mainteamdetaildata.MainTeamDetailResult

interface IMainTeamDetailView:IBaseView {

    fun onMianTeamDetailSuccessful(response:List<MainTeamDetailResult?>?)
}