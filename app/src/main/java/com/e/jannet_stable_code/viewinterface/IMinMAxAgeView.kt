package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.minmaxagedata.MinMaxAgeResult

interface IMinMAxAgeView:IBaseView {

    fun onMinMaxAgeSuccess(response:List<MinMaxAgeResult?>?)
}