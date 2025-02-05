package com.xtrane.retrofit.childinfodata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ChildInfoResult {

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("childGender")
    @Expose
    private var childGender: String? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("contactNo")
    @Expose
    private var contactNo: Any? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getChildGender(): String? {
        return childGender
    }

    fun setChildGender(childGender: String?) {
        this.childGender = childGender
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getContactNo(): Any? {
        return contactNo
    }

    fun setContactNo(contactNo: Any?) {
        this.contactNo = contactNo
    }

}