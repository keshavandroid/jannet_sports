package com.xtrane.viewinterface

import com.xtrane.retrofit.coachteamdata.CoachTeamResult

interface ICoachTeamListView:IBaseView {

    fun onCoachTeamListSuccess(response:List<CoachTeamResult?>?)

}