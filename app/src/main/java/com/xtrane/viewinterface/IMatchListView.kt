package com.xtrane.viewinterface

import com.xtrane.retrofit.matchlistdata.MatchListResult

interface IMatchListView:IBaseView {

    fun onMatchListSuccess(response:ArrayList<MatchListResult?>?)

}