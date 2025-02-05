package com.xtrane.retrofit.gettickitsdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GetTicketsBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<GetTicketsResullt?>? = null

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

    fun getResult(): List<GetTicketsResullt?>? {
        return result
    }

    fun setResult(result: List<GetTicketsResullt?>?) {
        this.result = result
    }
}