package com.xtrane.retrofit.coachlogindata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachLoginBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: CoachLoginResult? = null

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

    fun getResult(): CoachLoginResult? {
        return result
    }

    fun setResult(result: CoachLoginResult?) {
        this.result = result
    }

}