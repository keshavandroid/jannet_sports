package com.xtrane.retrofit.bookingdetaildata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class BookedMatchesResult {

    @SerializedName("time")
    @Expose
    private var time: String? = null

    @SerializedName("team_a_id")
    @Expose
    private var teamAId: String? = null

    @SerializedName("team_a_name")
    @Expose
    private var teamAName: String? = null

    @SerializedName("team_a_image")
    @Expose
    private var teamAImage: String? = null

    @SerializedName("team_b_id")
    @Expose
    private var teamBId: Any? = null

    @SerializedName("team_b_name")
    @Expose
    private var teamBName: String? = null

    @SerializedName("team_b_image")
    @Expose
    private var teamBImage: String? = null

    @SerializedName("coat")
    @Expose
    private var coat: String? = null

    @SerializedName("amount")
    @Expose
    private var amount: String? = null

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time
    }

    fun getTeamAId(): String? {
        return teamAId
    }

    fun setTeamAId(teamAId: String?) {
        this.teamAId = teamAId
    }

    fun getTeamAName(): String? {
        return teamAName
    }

    fun setTeamAName(teamAName: String?) {
        this.teamAName = teamAName
    }

    fun getTeamAImage(): String? {
        return teamAImage
    }

    fun setTeamAImage(teamAImage: String?) {
        this.teamAImage = teamAImage
    }

    fun getTeamBId(): Any? {
        return teamBId
    }

    fun setTeamBId(teamBId: Any?) {
        this.teamBId = teamBId
    }

    fun getTeamBName(): String? {
        return teamBName
    }

    fun setTeamBName(teamBName: String?) {
        this.teamBName = teamBName
    }

    fun getTeamBImage(): String? {
        return teamBImage
    }

    fun setTeamBImage(teamBImage: String?) {
        this.teamBImage = teamBImage
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