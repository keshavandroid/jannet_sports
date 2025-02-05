package com.xtrane.retrofit.teamdetaildata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class TeamDetailBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<TeamDetailResult?>? = null

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

    fun getResult(): List<TeamDetailResult?>? {
        return result
    }

    fun setResult(result: List<TeamDetailResult?>?) {
        this.result = result
    }
}