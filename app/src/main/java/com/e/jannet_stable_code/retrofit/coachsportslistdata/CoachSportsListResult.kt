package com.e.jannet_stable_code.retrofit.coachsportslistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachSportsListResult {


    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("sportsName")
    @Expose
    private var sportsName: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getSportsName(): String? {
        return sportsName
    }

    fun setSportsName(sportsName: String?) {
        this.sportsName = sportsName
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }
    override fun toString(): String {
        return "$sportsName"
    }
}