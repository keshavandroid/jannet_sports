package com.xtrane.viewinterface

import com.xtrane.retrofit.response.SportsListResponse

interface IParentHomeFilterView:IBaseView {
    fun onFilterListSuccess(sports:List<SportsListResponse.Result?>?)
}