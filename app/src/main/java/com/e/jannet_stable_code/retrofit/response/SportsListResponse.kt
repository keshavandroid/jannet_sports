package com.e.jannet_stable_code.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SportsListResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<Result?>? = null

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

    fun getResult(): List<Result?>? {
        return result
    }

    fun setResult(result: List<Result?>?) {
        this.result = result
    }

    class Result {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("sportsName")
        @Expose
        var sportsName: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null
        var isCheck: String = "0"
        var selected = false
        var selectedCoach = false
    }


}