package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.gradlistdata.GradeListResult

interface IGradeAgeView:IBaseView {

    fun onGradeAgeListSuccess(response:List<GradeListResult?>?)
}