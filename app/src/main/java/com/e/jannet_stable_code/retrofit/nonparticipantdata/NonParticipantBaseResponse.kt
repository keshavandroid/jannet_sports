package com.e.jannet_stable_code.retrofit.nonparticipantdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class NonParticipantBaseResponse {


    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<NonParticipanResult?>? = null

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

    fun getResult(): List<NonParticipanResult?>? {
        return result
    }

    fun setResult(result: List<NonParticipanResult?>?) {
        this.result = result
    }
}