package com.xtrane.viewinterface

import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.coachlogindata.CoachLoginBaseResponse

interface ICoachEventListView:IBaseView {

    fun onCoachEventListSuccess(response:List<CoachEventListResult?>?)

}