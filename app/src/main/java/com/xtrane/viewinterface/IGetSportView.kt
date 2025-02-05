package com.xtrane.viewinterface

import com.xtrane.retrofit.response.SportsListResponse

interface IGetSportView:IBaseView {

    fun onSportListSuccess(sports:List<SportsListResponse.Result?>?)
}