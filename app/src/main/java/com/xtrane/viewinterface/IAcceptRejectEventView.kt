package com.xtrane.viewinterface

import com.xtrane.retrofit.coacheventlistdata.CoachEventListResult
import com.xtrane.retrofit.coachlogindata.CoachLoginBaseResponse

interface IAcceptRejectEventView:IBaseView {

    fun onAcceptRejectEventSuccess()

    fun onAcceptRejectTicketSuccess()

}