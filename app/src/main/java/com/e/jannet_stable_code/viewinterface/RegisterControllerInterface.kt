package com.e.jannet_stable_code.viewinterface

interface RegisterControllerInterface {

    fun <T> onSuccess(response: T)
    fun onFail(error: String?)
    fun <T> onSuccessNew(response: T,method : String)
}