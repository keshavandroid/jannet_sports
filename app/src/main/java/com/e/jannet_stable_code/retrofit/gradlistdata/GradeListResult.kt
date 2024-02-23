package com.e.jannet_stable_code.retrofit.gradlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GradeListResult {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

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

    override fun toString(): String {
        return "$name"
    }
}

