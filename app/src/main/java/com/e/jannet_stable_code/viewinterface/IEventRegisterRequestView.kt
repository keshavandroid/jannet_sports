package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.coachlogindata.CoachLoginBaseResponse

interface IEventRegisterRequestView:IBaseView {

    fun onEventRegisterRequestSuccess(response:List<CoachEventListResult?>?, status: String)

}