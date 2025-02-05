package com.xtrane.viewinterface

import com.xtrane.retrofit.childinfodata.ChildInfoResult

interface IChildInfoView:IBaseView {

fun onChildInfoSuccess(response:List<ChildInfoResult?>?)
}