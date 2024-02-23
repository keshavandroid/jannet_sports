package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.coachlogindata.CoachLoginResult

interface ICoachLoginView:IBaseView {

    fun onCoachLoginSuccess(response:CoachLoginResult)
}