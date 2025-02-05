package com.xtrane.viewinterface

import com.xtrane.retrofit.response.EventDetailResponse

interface IEventDetailView:IBaseView {

    fun onEventDetailSuccess(response:EventDetailResponse.Result)
}