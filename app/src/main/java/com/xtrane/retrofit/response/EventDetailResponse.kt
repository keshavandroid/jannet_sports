package com.xtrane.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class EventDetailResponse :Serializable{
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
    class Image : Serializable{

        @SerializedName("id")
        @Expose
        private var id: Int? = null



        @SerializedName("image")
        @Expose
        private var image: String? = null

        fun getId(): Int? {
            return id
        }

        fun setId(id: Int?) {
            this.id = id
        }

        fun getImage(): String? {
            return image
        }

        fun setImage(image: String?) {
            this.image = image
        }


    }
    class Result : Serializable{

        @SerializedName("id")
        @Expose
        private var id: Int? = null

        @SerializedName("userId")
        @Expose
        private var userId: String? = null

        @SerializedName("eventName")
        @Expose
        private var eventName: String? = null

        @SerializedName("eventDate")
        @Expose
        private var eventDate: String? = null

        @SerializedName("sports_id")
        @Expose
        private var sportsId: String? = null

        @SerializedName("sportsName")
        @Expose
        private var sportsName: List<SportsName?>? = null

        @SerializedName("participants")
        @Expose
        private var participants: String? = null

        @SerializedName("gender_applicable")
        @Expose
        private var genderApplicable: String? = null

        @SerializedName("minAge")
        @Expose
        private var minAge: String? = null

        @SerializedName("maxAge")
        @Expose
        private var maxAge: String? = null

        @SerializedName("min_grade")
        @Expose
        private var min_grade: String? = null

        @SerializedName("max_grade")
        @Expose
        private var max_grade: String? = null



        @SerializedName("creator_name")
        @Expose
        private var creatorName: Any? = null

        @SerializedName("description")
        @Expose
        private var description: String? = null

        @SerializedName("fees")
        @Expose
        private var fees: String? = null

        @SerializedName("location_id")
        @Expose
        private var locationId: String? = null

        @SerializedName("address")
        @Expose
        private var address: String? = null

        @SerializedName("latitude")
        @Expose
        private var latitude: String? = null

        @SerializedName("longitude")
        @Expose
        private var longitude: String? = null

        @SerializedName("booked_user")
        @Expose
        private var bookedUser: List<Any?>? = null

        @SerializedName("grade_id")
        @Expose
        private var gradeId: String? = null

        @SerializedName("grade")
        @Expose
        private var grade: String? = null

        @SerializedName("mainimage")
        @Expose
        private var mainimage: String? = null

        @SerializedName("images")
        @Expose
        private var images: List<Image?>? = null

        @SerializedName("eventType")
        @Expose
        private var eventType: String? = null

        fun getCoachID(): String? {
            return userId
        }

        fun getEventType(): String? {
            return eventType
        }


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


        fun getMinGrade(): String? {
            return min_grade
        }

        fun setMinGrade(min_grade: String?) {
            this.min_grade = min_grade
        }


        fun getMaxGrade(): String? {
            return max_grade
        }

        fun setMaxGrade(max_grade: String?) {
            this.max_grade = max_grade
        }




        fun getSportsId(): String? {
            return sportsId
        }

        fun setSportsId(sportsId: String?) {
            this.sportsId = sportsId
        }

        fun getSportsName(): List<SportsName?>? {
            return sportsName
        }

        fun setSportsName(sportsName: List<SportsName?>?) {
            this.sportsName = sportsName
        }

        fun getParticipants(): String? {
            return participants
        }

        fun setParticipants(participants: String?) {
            this.participants = participants
        }

        fun getGenderApplicable(): String? {
            return genderApplicable
        }

        fun setGenderApplicable(genderApplicable: String?) {
            this.genderApplicable = genderApplicable
        }

        fun getMinAge(): String? {
            return minAge
        }

        fun setMinAge(minAge: String?) {
            this.minAge = minAge
        }

        fun getMaxAge(): String? {
            return maxAge
        }

        fun setMaxAge(maxAge: String?) {
            this.maxAge = maxAge
        }

        fun getCreatorName(): Any? {
            return creatorName
        }

        fun setCreatorName(creatorName: Any?) {
            this.creatorName = creatorName
        }

        fun getDescription(): String? {
            return description
        }

        fun setDescription(description: String?) {
            this.description = description
        }

        fun getFees(): String? {
            return fees
        }

        fun setFees(fees: String?) {
            this.fees = fees
        }

        fun getLocationId(): String? {
            return locationId
        }

        fun setLocationId(locationId: String?) {
            this.locationId = locationId
        }

        fun getAddress(): String? {
            return address
        }

        fun setAddress(address: String?) {
            this.address = address
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

        fun getBookedUser(): List<Any?>? {
            return bookedUser
        }

        fun setBookedUser(bookedUser: List<Any?>?) {
            this.bookedUser = bookedUser
        }

        fun getGradeId(): String? {
            return gradeId
        }

        fun setGradeId(gradeId: String?) {
            this.gradeId = gradeId
        }

        fun getGrade(): String? {
            return grade
        }

        fun setGrade(grade: String?) {
            this.grade = grade
        }

        fun getMainimage(): String? {
            return mainimage
        }

        fun setMainimage(mainimage: String?) {
            this.mainimage = mainimage
        }

        fun getImages(): List<Image?>? {
            return images
        }

        fun setImages(images: List<Image?>?) {
            this.images = images
        }

    }

    class SportsName : Serializable{
//        @SerializedName("sportsName")
//        @Expose
//        var sportsName: String? = null

        @SerializedName("sportsName")
        @Expose
        private var sportsName: String? = null

        fun getSportsName(): String? {
            return sportsName
        }

        fun setSportsName(sportsName: String?) {
            this.sportsName = sportsName
        }
    }


}