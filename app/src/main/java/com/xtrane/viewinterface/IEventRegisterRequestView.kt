package com.xtrane.viewinterface

import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.coachlogindata.CoachLoginBaseResponse

interface IEventRegisterRequestView:IBaseView {

    fun onEventRegisterRequestSuccess(response:List<CoachEventListResult?>?, status: String)

}