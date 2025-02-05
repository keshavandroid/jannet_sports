package com.xtrane.retrofit.coachfilterdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachFilterResult {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("eventName")
    @Expose
    private var eventName: String? = null

    @SerializedName("eventDate")
    @Expose
    private var eventDate: String? = null

    @SerializedName("fees")
    @Expose
    private var fees: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("isInSports")
    @Expose
    private var isInSports: String? = null

    @SerializedName("isEventAvailable")
    @Expose
    private var isEventAvailable: Int? = null

    @SerializedName("imageId")
    @Expose
    private var imageId: List<Any?>? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getEventName(): String? {
        return eventName
    }

    fun setEventName(eventName: String?) {
        this.eventName = eventName
    }

    fun getEventDate(): String? {
        return eventDate
    }

    fun setEventDate(eventDate: String?) {
        this.eventDate = eventDate
    }

    fun getFees(): String? {
        return fees
    }

    fun setFees(fees: String?) {
        this.fees = fees
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getIsInSports(): String? {
        return isInSports
    }

    fun setIsInSports(isInSports: String?) {
        this.isInSports = isInSports
    }

    fun getIsEventAvailable(): Int? {
        return isEventAvailable
    }

    fun setIsEventAvailable(isEventAvailable: Int?) {
        this.isEventAvailable = isEventAvailable
    }

    fun getImageId(): List<Any?>? {
        return imageId
    }

    fun setImageId(imageId: List<Any?>?) {
        this.imageId = imageId
    }
}