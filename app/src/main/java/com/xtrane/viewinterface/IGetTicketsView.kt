package com.xtrane.viewinterface

import com.xtrane.retrofit.gettickitsdata.GetTicketsResullt

interface IGetTicketsView:IBaseView {

    fun onGetTicktetsSuccess(response:List<GetTicketsResullt?>?)
}