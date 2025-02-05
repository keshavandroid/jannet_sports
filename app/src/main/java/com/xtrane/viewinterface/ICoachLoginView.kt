package com.xtrane.viewinterface

import com.xtrane.retrofit.coachlogindata.CoachLoginResult

interface ICoachLoginView:IBaseView {

    fun onCoachLoginSuccess(response:CoachLoginResult)
}