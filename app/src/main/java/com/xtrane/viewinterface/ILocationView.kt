package com.xtrane.viewinterface

import com.xtrane.retrofit.locationdata.Coat
import com.xtrane.retrofit.locationdata.LocationResult

interface ILocationView:IBaseView {

    fun onLocationListSuccess(response:List<LocationResult?>?)
    fun onCoatListSuccess(response: List<Coat?>?)
}