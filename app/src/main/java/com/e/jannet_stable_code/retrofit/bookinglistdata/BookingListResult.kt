package com.e.jannet_stable_code.retrofit.bookinglistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class BookingListResult {

    @SerializedName("book_id")
    @Expose
    private var bookId: Int? = null

    @SerializedName("userName")
    @Expose
    private var userName: String? = null

    @SerializedName("userImage")
    @Expose
    private var userImage: String? = null

    @SerializedName("noOfMatch")
    @Expose
    private var noOfMatch: Int? = null

    @SerializedName("noOfTicket")
    @Expose
    private var noOfTicket: String? = null

    @SerializedName("coat")
    @Expose
    private var coat: String? = null

    @SerializedName("amount")
    @Expose
    private var amount: String? = null

    fun getBookId(): Int? {
        return bookId
    }

    fun setBookId(bookId: Int?) {
        this.bookId = bookId
    }

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

    fun getNoOfMatch(): Int? {
        return noOfMatch
    }

    fun setNoOfMatch(noOfMatch: Int?) {
        this.noOfMatch = noOfMatch
    }

    fun getNoOfTicket(): String? {
        return noOfTicket
    }

    fun setNoOfTicket(noOfTicket: String?) {
        this.noOfTicket = noOfTicket
    }

    fun getCoat(): String? {
        return coat
    }

    fun setCoat(coat: String?) {
        this.coat = coat
    }

    fun getAmount(): String? {
        return amount
    }

    fun setAmount(amount: String?) {
        this.amount = amount
    }
}