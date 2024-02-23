package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.response.GetProfileParentApiResponse

interface IProfileView:IBaseView {

    fun onProfileSuccess(response:List<GetProfileParentApiResponse.Result?>?)
}