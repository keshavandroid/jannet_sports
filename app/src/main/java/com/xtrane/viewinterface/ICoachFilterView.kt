package com.xtrane.viewinterface

import com.xtrane.retrofit.coachfilterdata.CoachFilterResult

interface ICoachFilterView:IBaseView {

    fun onCoachFilterSuccess(response:List<CoachFilterResult?>?)
}