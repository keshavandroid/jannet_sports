package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.gettickitsdata.GetTicketsResullt

interface IGetTicketsView:IBaseView {

    fun onGetTicktetsSuccess(response:List<GetTicketsResullt?>?)
}