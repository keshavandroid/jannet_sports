package com.xtrane.retrofit.coachteamdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachTeamBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<CoachTeamResult?>? = null

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

    fun getResult(): List<CoachTeamResult?>? {
        return result
    }

    fun setResult(result: List<CoachTeamResult?>?) {
        this.result = result
    }


}