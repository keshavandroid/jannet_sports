package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.childinfodata.ChildInfoResult

interface IChildInfoView:IBaseView {

fun onChildInfoSuccess(response:List<ChildInfoResult?>?)
}