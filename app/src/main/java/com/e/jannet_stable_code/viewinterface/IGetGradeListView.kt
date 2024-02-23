package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.gradlistdata.GradeListResult

interface IGetGradeListView:IBaseView {

    fun onGradeListSuccess(response:List<GradeListResult?>?)
}