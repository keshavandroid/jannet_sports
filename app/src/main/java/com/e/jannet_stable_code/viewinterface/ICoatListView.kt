package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.locationdata.Coat

interface ICoatListView:IBaseView {

    fun onCOatListSuccess(response:List<Coat?>?)
}