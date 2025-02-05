package com.xtrane.viewinterface

import com.xtrane.retrofit.locationdata.Coat

interface ICoatListView:IBaseView {

    fun onCOatListSuccess(response:List<Coat?>?)
}