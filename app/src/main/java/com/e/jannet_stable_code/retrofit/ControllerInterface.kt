package com.e.jannet_stable_code.retrofit

interface ControllerInterface {
    fun onFail(error: String?)
    fun <T> onSuccess(response: T,method : String)
}