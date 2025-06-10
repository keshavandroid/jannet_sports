package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PurchaseHistoryResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: Result?= null

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
        @SerializedName("totalBalance")
        @Expose
        var totalBalance: String? = null

        @SerializedName("usedBalance")
        @Expose
        var usedBalance: String? = null

        @SerializedName("currentBalance")
        @Expose
        var currentBalance: String? = null

        @SerializedName("history")
        @Expose
        var history: List<historyData?>? = null

    }

    class historyData
    {
        @SerializedName("balanceId")
        @Expose
        var balanceId:Int? = null

        @SerializedName("currency")
        @Expose
        var currency: String? = null

        @SerializedName("purchasedBalance")
        @Expose
        var purchasedBalance: String? = null

        @SerializedName("purchadedDate")
        @Expose
        var purchasedDate: String? = null
    }


}