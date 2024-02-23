package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.locationdata.Coat
import com.e.jannet_stable_code.retrofit.locationdata.LocationResult

interface ILocationView:IBaseView {

    fun onLocationListSuccess(response:List<LocationResult?>?)
    fun onCoatListSuccess(response: List<Coat?>?)
}