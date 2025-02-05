package com.xtrane.viewinterface

import com.xtrane.retrofit.response.GetProfileParentApiResponse

interface IProfileView:IBaseView {

    fun onProfileSuccess(response:List<GetProfileParentApiResponse.Result?>?)
}