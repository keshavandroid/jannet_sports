package com.xtrane.retrofit.coacheventlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachEventImageID {

    @SerializedName("id")
    @Expose
    private var id: Int? = null
    private var image: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }
    fun getImage(): String? {
        return image
    }

    fun setImage(id: String?) {
        this.image = id
    }
}