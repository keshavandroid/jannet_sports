package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.response.SportsListResponse

interface IGetSportView:IBaseView {

    fun onSportListSuccess(sports:List<SportsListResponse.Result?>?)
}