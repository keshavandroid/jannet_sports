package com.e.jannet_stable_code.retrofit.addeventBaseResponse

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AddEventResult {


    @SerializedName("eventId")
    @Expose
    private var eventId: Int? = null

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    fun getEventId(): Int? {
        return eventId
    }

    fun setEventId(eventId: Int?) {
        this.eventId = eventId
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

}