package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.xtrane.utils.SharedPrefUserData

class LoginResponse{
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
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("firstname")
        @Expose
        var firstname: String? = null

        @SerializedName("lastname")
        @Expose
        var lastname: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("birthdate")
        @Expose
        var birthdate: String? = null

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

        @SerializedName("status")
        @Expose
        var status: String? = null
    }
    data class SavedData(
        var id: String? = null,
        var firstname: String? = null,
        var lastname: String? = null,
        var gender: String? = null,
        var birthdate: String? = null,
        var email: String? = null,
        var password: String? = null,
        var contactno: String? = null,
        var usertype: String? = null,
        var image: String? = null,
        var token: String? = null,
        var registerStep: String? = null,
        var location: String? = null,
        var sportList: ArrayList<SharedPrefUserData.Sports>? = null
    )
}