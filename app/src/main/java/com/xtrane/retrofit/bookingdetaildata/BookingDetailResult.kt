package com.xtrane.retrofit.bookingdetaildata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class BookingDetailResult {


    @SerializedName("userName")
    @Expose
    private var userName: String? = null

    @SerializedName("userImage")
    @Expose
    private var userImage: String? = null

    @SerializedName("noOfTicket")
    @Expose
    private var noOfTicket: String? = null

    @SerializedName("noOfMatch")
    @Expose
    private var noOfMatch: String? = null

    @SerializedName("amount")
    @Expose
    private var amount: String? = null

    @SerializedName("bookedMatches")
    @Expose
    private var bookedMatches: List<BookedMatchesResult?>? = null

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String?) {
        this.userName = userName
    }

    fun getUserImage(): String? {
        return userImage
    }

    fun setUserImage(userImage: String?) {
        this.userImage = userImage
    }

    fun getNoOfTicket(): String? {
        return noOfTicket
    }

    fun setNoOfTicket(noOfTicket: String?) {
        this.noOfTicket = noOfTicket
    }

    fun getNoOfMatch(): String? {
        return noOfMatch
    }

    fun setNoOfMatch(noOfMatch: String?) {
        this.noOfMatch = noOfMatch
    }

    fun getAmount(): String? {
        return amount
    }

    fun setAmount(amount: String?) {
        this.amount = amount
    }

    fun getBookedMatches(): List<BookedMatchesResult?>? {
        return bookedMatches
    }

    fun setBookedMatches(bookedMatches: List<BookedMatchesResult?>?) {
        this.bookedMatches = bookedMatches
    }

}