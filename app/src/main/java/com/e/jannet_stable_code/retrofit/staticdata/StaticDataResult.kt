package com.e.jannet_stable_code.retrofit.staticdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class StaticDataResult {

    @SerializedName("Title")
    @Expose
    private var title: String? = null

    @SerializedName("Content")
    @Expose
    private var content: String? = null

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }


}