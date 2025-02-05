package com.xtrane.viewinterface

import com.xtrane.retrofit.minmaxagedata.MinMaxAgeResult

interface IMinMAxAgeView:IBaseView {

    fun onMinMaxAgeSuccess(response:List<MinMaxAgeResult?>?)
}