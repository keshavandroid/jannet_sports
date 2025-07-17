package com.xtrane.retrofit.coacheventlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
class CoachEventListResult {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("eventName")
    @Expose
    private var eventName: String? = null

    @SerializedName("eventDate")
    @Expose
    private var eventDate: String? = null

    @SerializedName("eventTime")
    @Expose
    private var eventTime: String? = null

    @SerializedName("fees")
    @Expose
    private var fees: String? = null

    @SerializedName("user_id")
    @Expose
    private var user_id: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("isInSports")
    @Expose
    private var isInSports: String? = null

    @SerializedName("child_id")
    @Expose
    private var child_id: String? = null

    @SerializedName("child_name")
    @Expose
    private var child_name: String? = null

    @SerializedName("role")
    @Expose
    private var role: String? = null

    @SerializedName("user_name")
    @Expose
    private var user_name: String? = null


    @SerializedName("match_id")
    @Expose
    private var match_id: String? = null

    @SerializedName("count")
    @Expose
    private var count: Int? = null

    @SerializedName("isEventAvailable")
    @Expose
    private var isEventAvailable: Int? = null

    @SerializedName("imageId")
    @Expose
    private var imageId: List<CoachEventImageID?>? = null

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

    fun getRole(): String? {
        return role
    }
    fun setRole(role: String?) {
        this.role = role
    }
    fun getEventDate(): String? {
        return eventDate
    }

    fun setEventDate(eventDate: String?) {
        this.eventDate = eventDate
    }

    fun getEventTime(): String? {
        return eventTime
    }

    fun setEventTime(eventdate: String?) {
        this.eventTime = eventdate
    }
    fun getFees(): String? {
        return fees
    }

    fun setFees(fees: String?) {
        this.fees = fees
    }

    fun getUserID(): String? {
        return user_id
    }

    fun setUserID(user_id: String?) {
        this.user_id = user_id
    }

    fun getUserName(): String? {
        return user_name
    }

    fun setUserName(user_name: String?) {
        this.user_name = user_name
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

    fun getImageId(): List<CoachEventImageID?>? {
        return imageId
    }

    fun setImageId(imageId: List<CoachEventImageID?>?) {
        this.imageId = imageId
    }


    fun getChildId(): String? {
        return child_id
    }

    fun setChildId(child_id: String?) {
        this.child_id = child_id
    }

    fun getChildName(): String? {
        return child_name
    }

    fun setChildName(child_name: String?) {
        this.child_name = child_name
    }

    fun getCount(): Int? {
        return count
    }

    fun getCount(count: Int?) {
        this.count = count
    }

    fun getMatchId(): String? {
        return match_id
    }

    fun setMatchId(match_id: String?) {
        this.match_id = match_id
    }
    fun getEventdate(): String? {

        return  eventDate
    }

}
//
//list of >
//id, eventName,
//eventDate, image, isInSports
//isEventAvailable, fees
//imageId[ {id : } ]