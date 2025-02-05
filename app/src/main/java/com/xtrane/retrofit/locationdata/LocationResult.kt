package com.xtrane.retrofit.locationdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class LocationResult {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("isCheck")
    @Expose
    private var isCheck: String? = "0"

    @SerializedName("latitude")
    @Expose
    private var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    private var longitude: String? = null

    @SerializedName("coat")
    @Expose
    private var coat: List<Coat?>? = null

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
   fun getisCheck(): String? {
        return isCheck
    }

    fun setisCheck(isCheck: String?) {
        this.isCheck = isCheck
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?) {
        this.longitude = longitude
    }

    fun getCoat(): List<Coat?>? {
        return coat
    }

    fun setCoat(coat: List<Coat?>?) {
        this.coat = coat
    }

    override fun toString(): String {
        return "$name"
    }
}