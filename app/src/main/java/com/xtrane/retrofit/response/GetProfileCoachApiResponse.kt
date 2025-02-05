package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetProfileCoachApiResponse {
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

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("firstname")
        @Expose
        var firstname: Any? = null

        @SerializedName("lastname")
        @Expose
        var lastname: Any? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("birthdate")
        @Expose
        var birthdate: String? = null

        @SerializedName("location")
        @Expose
        var location: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("contactNo")
        @Expose
        var contactNo: String? = null

        @SerializedName("userType")
        @Expose
        var userType: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("userToken")
        @Expose
        var userToken: String? = null

        @SerializedName("sports")
        @Expose
        var sports: List<Sport>? = null
    }
    class Sport {
        @SerializedName("sportsName")
        @Expose
        var sportsName: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = ""

        @SerializedName("id")
        @Expose
        var id: Int? = null
    }
}