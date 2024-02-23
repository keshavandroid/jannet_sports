package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.coachsportslistdata.CoachSportsListResult

interface ICoachSportsListVIew:IBaseView {

    fun onGetSportsListSuccess(response:List<CoachSportsListResult?>?)
}
