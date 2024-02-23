package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.e.jannet_stable_code.retrofit.coachlogindata.CoachLoginBaseResponse

interface IAcceptRejectEventView:IBaseView {

    fun onAcceptRejectEventSuccess()

    fun onAcceptRejectTicketSuccess()

}