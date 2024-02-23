package com.e.jannet_stable_code.retrofit.mainteamdetaildata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MainTeamParticipentList {

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