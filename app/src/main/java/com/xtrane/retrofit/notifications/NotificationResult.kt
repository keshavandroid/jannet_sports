package com.xtrane.retrofit.notifications

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class NotificationResult {

    @SerializedName("userName")
    @Expose
    private var userName: String? = null

    @SerializedName("userImage")
    @Expose
    private var userImage: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null



    fun getUsername(): String? {
        return userName
    }

    fun setUsername(userName: String?) {
        this.userName = userName
    }

    fun getUserImage(): String? {
        return userImage
    }

    fun setUserImage(userImage: String?) {
        this.userImage = userImage
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }



}