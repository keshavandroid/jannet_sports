package com.xtrane.retrofit.minmaxagedata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MinMaxAgeResult {
    @SerializedName("minAgeStart")
    @Expose
    private var minAgeStart: Int? = null

    @SerializedName("minAgeEnd")
    @Expose
    private var minAgeEnd: Int? = null

    @SerializedName("maxAgeStart")
    @Expose
    private var maxAgeStart: Int? = null

    @SerializedName("maxAgeEnd")
    @Expose
    private var maxAgeEnd: Int? = null

    fun getMinAgeStart(): Int? {
        return minAgeStart
    }

    fun setMinAgeStart(minAgeStart: Int?) {
        this.minAgeStart = minAgeStart
    }

    fun getMinAgeEnd(): Int? {
        return minAgeEnd
    }

    fun setMinAgeEnd(minAgeEnd: Int?) {
        this.minAgeEnd = minAgeEnd
    }

    fun getMaxAgeStart(): Int? {
        return maxAgeStart
    }

    fun setMaxAgeStart(maxAgeStart: Int?) {
        this.maxAgeStart = maxAgeStart
    }

    fun getMaxAgeEnd(): Int? {
        return maxAgeEnd
    }

    fun setMaxAgeEnd(maxAgeEnd: Int?) {
        this.maxAgeEnd = maxAgeEnd
    }

}