package com.xtrane.viewinterface

interface IBaseView {

    fun showLoader()
    fun showLoader(message: String?)
    fun hideLoader()
    fun onFail(message: String?, e: Exception?)

}