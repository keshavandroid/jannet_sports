package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.MainTeamListdata.MainTeamListResult
import com.e.jannet_stable_code.retrofit.mainteamdetaildata.MainTeamDetailResult

interface IMainTeamDetailView:IBaseView {

    fun onMianTeamDetailSuccessful(response:List<MainTeamDetailResult?>?)
}