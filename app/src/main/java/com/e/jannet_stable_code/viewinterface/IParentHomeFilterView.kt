package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.response.SportsListResponse

interface IParentHomeFilterView:IBaseView {
    fun onFilterListSuccess(sports:List<SportsListResponse.Result?>?)
}