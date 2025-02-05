package com.xtrane.retrofit

interface ControllerInterface {
    fun onFail(error: String?)
    fun <T> onSuccess(response: T,method : String)
}