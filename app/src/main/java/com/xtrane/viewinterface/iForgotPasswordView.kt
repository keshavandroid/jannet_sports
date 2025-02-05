package com.xtrane.viewinterface

import com.xtrane.retrofit.forgotpassworddata

interface iForgotPasswordView:IBaseView {

    fun onForgotPasswordSuccess(response: forgotpassworddata)
}