package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PrivacyPolicyResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: Result? = null

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

    fun getResult(): Result? {
        return result
    }

    fun setResult(result: Result?) {
        this.result = result
    }
    class Result {
        @SerializedName("Title")
        @Expose
        var title: String? = null

        @SerializedName("Content")
        @Expose
        var content: String? = null
    }
}