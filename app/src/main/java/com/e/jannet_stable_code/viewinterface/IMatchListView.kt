package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.matchlistdata.MatchListResult

interface IMatchListView:IBaseView {

    fun onMatchListSuccess(response:ArrayList<MatchListResult?>?)

}