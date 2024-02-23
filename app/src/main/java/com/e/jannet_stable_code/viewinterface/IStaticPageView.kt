package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.staticdata.StaticDataResult

interface IStaticPageView:IBaseView {

    fun onStaticDataSuccess(Response:StaticDataResult)
}