package com.xtrane.viewinterface

import com.xtrane.retrofit.gradlistdata.GradeListResult

interface IGetGradeListView:IBaseView {

    fun onGradeListSuccess(response:List<GradeListResult?>?)
}