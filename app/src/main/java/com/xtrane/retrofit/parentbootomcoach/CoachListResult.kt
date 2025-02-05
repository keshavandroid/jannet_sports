package com.xtrane.retrofit.parentbootomcoach

import com.xtrane.retrofit.response.EventDetailResponse
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachListResult {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("gender")
    @Expose
    private var gender: String? = null

    @SerializedName("birthdate")
    @Expose
    private var birthdate: String? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("contactNo")
    @Expose
    private var contactNo: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("userToken")
    @Expose
    private var userToken: String? = null

    @SerializedName("sportsName")
    @Expose
    private var sportsName: List<SportsName?>? = null

    @SerializedName("sportsname")
    @Expose
    private var sportsname: List<SportsName?>? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getSportsName(): List<SportsName?>? {
        return sportsName
    }

    fun setSportsName(sportsName: List<SportsName?>?) {
        this.sportsName = sportsName
    }

    fun getSportsname(): List<SportsName?>? {
        return sportsname
    }

    fun setSportsname(sportsName: List<SportsName?>?) {
        this.sportsname = sportsName
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String?) {
        this.gender = gender
    }

    fun getBirthdate(): String? {
        return birthdate
    }

    fun setBirthdate(birthdate: String?) {
        this.birthdate = birthdate
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getContactNo(): String? {
        return contactNo
    }

    fun setContactNo(contactNo: String?) {
        this.contactNo = contactNo
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getUserToken(): String? {
        return userToken
    }

    fun setUserToken(userToken: String?) {
        this.userToken = userToken
    }

    class SportsName {

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