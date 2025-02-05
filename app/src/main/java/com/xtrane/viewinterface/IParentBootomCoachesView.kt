package com.xtrane.viewinterface

import com.xtrane.retrofit.parentbootomcoach.CoachListResult

interface IParentBootomCoachesView:IBaseView {

    fun onBottomCoachesListSuccess(response:List<CoachListResult?>?)
}