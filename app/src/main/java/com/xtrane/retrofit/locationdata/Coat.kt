package com.xtrane.retrofit.locationdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Coat {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("coatName")
    @Expose
    private var coatName: String? = null

    @SerializedName("coatStrength")
    @Expose
    private var coatStrength: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getCoatName(): String? {
        return coatName
    }

    fun setCoatName(coatName: String?) {
        this.coatName = coatName
    }

    fun getCoatStrength(): String? {
        return coatStrength
    }

    fun setCoatStrength(coatStrength: String?) {
        this.coatStrength = coatStrength
    }

    override fun toString(): String {
        return "$id"
    }
}