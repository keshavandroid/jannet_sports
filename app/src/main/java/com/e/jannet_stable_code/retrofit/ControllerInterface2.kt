package com.e.jannet_stable_code.retrofit

interface ControllerInterface2 {
    fun onFailResponse(error: String?)
    fun <T> onSuccessResponse(response: T,method : String)
}