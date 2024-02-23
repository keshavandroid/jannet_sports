package com.e.jannet_stable_code.retrofit.coacheventlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachEventImageID {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }
}