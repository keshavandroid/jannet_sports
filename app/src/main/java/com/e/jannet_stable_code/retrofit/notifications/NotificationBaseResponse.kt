package com.e.jannet_stable_code.retrofit.notifications

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class NotificationBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: ArrayList<NotificationResult?>? = null

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

    fun getResult(): ArrayList<NotificationResult?>? {
        return result
    }

    fun setResult(result: ArrayList<NotificationResult?>?) {
        this.result = result
    }
}