package com.xtrane.retrofit

interface ControllerInterface2 {
    fun onFailResponse(error: String?)
    fun <T> onSuccessResponse(response: T,method : String)
}