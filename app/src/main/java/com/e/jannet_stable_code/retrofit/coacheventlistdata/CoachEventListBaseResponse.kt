package com.e.jannet_stable_code.retrofit.coacheventlistdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CoachEventListBaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<CoachEventListResult?>? = null

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

    fun getResult(): List<CoachEventListResult?>? {
        return result
    }

    fun setResult(result: List<CoachEventListResult?>?) {
        this.result = result
    }

}