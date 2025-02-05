package com.xtrane.viewinterface

import com.xtrane.retrofit.coachsportslistdata.CoachSportsListResult

interface ICoachSportsListVIew:IBaseView {

    fun onGetSportsListSuccess(response:List<CoachSportsListResult?>?)
}
