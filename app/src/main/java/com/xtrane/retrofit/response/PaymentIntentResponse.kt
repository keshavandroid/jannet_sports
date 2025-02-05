package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentIntentResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("payment_intent_id")
    @Expose
    private var payment_intent_id: String? = null

    @SerializedName("payment_intent_client_secret")
    @Expose
    private var payment_intent_client_secret: String? = null

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

    fun getPaymentIntentId(): String? {
        return payment_intent_id
    }

    fun setPaymentIntentId(payment_intent_id: String?) {
        this.payment_intent_id = payment_intent_id
    }

    fun getPaymentIntentClientSecret(): String? {
        return payment_intent_client_secret
    }

    fun setPaymentIntentClientSecret(payment_intent_client_secret: String?) {
        this.payment_intent_client_secret = payment_intent_client_secret
    }

}