package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.response.EventDetailResponse

interface IEventDetailView:IBaseView {

    fun onEventDetailSuccess(response:EventDetailResponse.Result)
}