package com.e.jannet_stable_code.retrofit.matchlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MatchListBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: ArrayList<MatchListResult?>? = null

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

    fun getResult(): ArrayList<MatchListResult?>? {
        return result
    }

    fun setResult(result: ArrayList<MatchListResult?>?) {
        this.result = result
    }
}