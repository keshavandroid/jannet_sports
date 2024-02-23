package com.e.jannet_stable_code.retrofit.bookinglistdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BookTicketDetailResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<BookTicketDetailResult?>? = null

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

    fun getResult(): List<BookTicketDetailResult?>? {
        return result
    }

    fun setResult(result: List<BookTicketDetailResult?>?) {
        this.result = result
    }


}