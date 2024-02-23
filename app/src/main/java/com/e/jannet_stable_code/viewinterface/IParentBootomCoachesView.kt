package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.parentbootomcoach.CoachListResult

interface IParentBootomCoachesView:IBaseView {

    fun onBottomCoachesListSuccess(response:List<CoachListResult?>?)
}