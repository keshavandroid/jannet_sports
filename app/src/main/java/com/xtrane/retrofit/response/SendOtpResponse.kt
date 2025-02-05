package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendOtpResponse{
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("emailOtp")
    @Expose
    private var emailOtp: Int? = null

    @SerializedName("phoneOtp")
    @Expose
    private var phoneOtp: String? = null

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

    fun getEmailOtp(): Int? {
        return emailOtp
    }

    fun setEmailOtp(emailOtp: Int?) {
        this.emailOtp = emailOtp
    }

    fun getPhoneOtp(): String? {
        return phoneOtp
    }

    fun setPhoneOtp(phoneOtp: String?) {
        this.phoneOtp = phoneOtp
    }
}