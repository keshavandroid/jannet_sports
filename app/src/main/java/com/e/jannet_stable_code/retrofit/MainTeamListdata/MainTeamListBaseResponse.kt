package com.e.jannet_stable_code.retrofit.MainTeamListdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MainTeamListBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<MainTeamListResult?>? = null

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

    fun getResult(): List<MainTeamListResult?>? {
        return result
    }

    fun setResult(result: List<MainTeamListResult?>?) {
        this.result = result
    }

}