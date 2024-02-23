package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.coachteamdata.CoachTeamResult

interface ICoachTeamListView:IBaseView {

    fun onCoachTeamListSuccess(response:List<CoachTeamResult?>?)

}