package com.xtrane.viewinterface

import com.xtrane.retrofit.gradlistdata.GradeListResult

interface IGradeAgeView:IBaseView {

    fun onGradeAgeListSuccess(response:List<GradeListResult?>?)
}