package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.coachfilterdata.CoachFilterResult

interface ICoachFilterView:IBaseView {

    fun onCoachFilterSuccess(response:List<CoachFilterResult?>?)
}