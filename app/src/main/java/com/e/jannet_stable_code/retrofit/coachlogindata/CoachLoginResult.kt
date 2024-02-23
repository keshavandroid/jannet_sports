package com.e.jannet_stable_code.retrofit.coachlogindata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachLoginResult {

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

    @SerializedName("userType")
    @Expose
    private var userType: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("userToken")
    @Expose
    private var userToken: String? = null

    @SerializedName("location")
    @Expose
    private var location: String? = null

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

    fun getUserType(): String? {
        return userType
    }

    fun setUserType(userType: String?) {
        this.userType = userType
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

    fun getLocation(): String? {
        return location
    }

    fun setLocation(location: String?) {
        this.location = location
    }

}