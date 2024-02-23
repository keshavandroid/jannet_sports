package com.e.jannet_stable_code.retrofit.parentbootomcoach

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ParentBootomCoacheBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<CoachListResult?>? = null

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

    fun getResult(): List<CoachListResult?>? {
        return result
    }

    fun setResult(result: List<CoachListResult?>?) {
        this.result = result
    }
}