package com.xtrane.retrofit.response

import com.xtrane.retrofit.coacheventlistdata.CoachEventImageID
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventListResponse {
    @SerializedName("status")
    @Expose
    private var status: Int? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("result")
    @Expose
    private var result: List<Result?>? = null

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getResult(): List<Result?>? {
        return result
    }

    fun setResult(result: List<Result?>?) {
        this.result = result
    }
    class Result {


        @SerializedName("id")
        @Expose
        private var id: Int? = null

        @SerializedName("eventName")
        @Expose
        private var eventName: String? = null

        @SerializedName("eventTime")
        @Expose
        private var eventTime: String? = null

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

        fun getEventDate(): String? {
            return eventDate
        }

        fun setEventDate(eventDate: String?) {
            this.eventDate = eventDate
        }
        fun getEventTime(): String? {
            return eventTime
        }
        fun setEventTime(eventTime: String?) {
            this.eventTime = eventTime
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

        fun getImageId(): List<CoachEventImageID?>? {
            return imageId
        }

        fun setImageId(imageId: List<CoachEventImageID?>?) {
            this.imageId = imageId
        }



    }
}