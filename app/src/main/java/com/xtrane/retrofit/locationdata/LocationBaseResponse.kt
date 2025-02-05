package com.xtrane.retrofit.locationdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LocationBaseResponse {


    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<LocationResult?>? = null

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getResult(): List<LocationResult?>? {
        return result
    }

    fun setResult(result: List<LocationResult?>?) {
        this.result = result
    }

}