package com.xtrane.viewinterface

import com.xtrane.retrofit.staticdata.StaticDataResult

interface IStaticPageView:IBaseView {

    fun onStaticDataSuccess(Response:StaticDataResult)
}