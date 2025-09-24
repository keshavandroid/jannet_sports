package com.xtrane.retrofit.nonparticipantdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class GetMemberResult {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<MemberResult?>? = null

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

    fun getResult(): List<MemberResult?>? {
        return result
    }

    fun setResult(result: List<MemberResult?>?) {
        this.result = result
    }

    class MemberResult {
        @SerializedName("id")
        @Expose
        private var id: Int? = null

        @SerializedName("name")
        @Expose
        private var name: String? = null

        @SerializedName("image")
        @Expose
        private var image: String? = null


        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }

        fun getName(): String? {
            return name
        }

        fun setName(name: String?) {
            this.name = name
        }

        fun getImage(): String? {
            return image
        }

        fun setImage(image: String?) {
            this.image = image
        }
    }
}