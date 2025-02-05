package com.xtrane.retrofit.matchlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class MatchListResult {

    @SerializedName("matchId")
    @Expose
    private var matchId: Int? = null

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
    private var teamBId: String? = null

    @SerializedName("team_b_name")
    @Expose
    private var teamBName: String? = null

    @SerializedName("team_b_image")
    @Expose
    private var teamBImage: String? = null

    @SerializedName("coat")
    @Expose
    private var coat: String? = null

    @SerializedName("date")
    @Expose
    private var date: String? = null

    @SerializedName("time")
    @Expose
    private var time: String? = null

    @SerializedName("matchPrice")
    @Expose
    private var matchPrice: String? = "0"

    @SerializedName("price")
    @Expose
    private var price: String? = null

    @SerializedName("eventName")
    @Expose
    private var eventName: String? = null

    @SerializedName("eventImage")
    @Expose
    private var eventImage: String? = null

    @SerializedName("eventPrice")
    @Expose
    private var eventPrice: String? = null

    var isCheck: Boolean = false

    fun getMatchId(): Int? {
        return matchId
    }

    fun setMatchId(matchId: Int?) {
        this.matchId = matchId
    }

    fun getTeamAId(): String? {
        return teamAId
    }

    fun setTeamAId(teamAId: String?) {
        this.teamAId = teamAId
    }

    fun getPrice(): String? {
        return price
    }

    fun setPrice(price: String?) {
        this.price = price
    }

    fun getEventName(): String? {
        return eventName
    }

    fun setEventName(eventName: String?) {
        this.eventName = eventName
    }
    fun getEventImage(): String? {
        return eventImage
    }

    fun setEventImage(eventImage: String?) {
        this.eventImage = eventImage
    }

    fun getMatchPrice(): String? {
        return matchPrice
    }

    fun setmatchPrice(matchPrice: String?) {
        this.matchPrice = matchPrice
    }

    fun getEventPrice(): String? {
        return eventPrice
    }

    fun setEventPrice(eventPrice: String?) {
        this.eventPrice = eventPrice
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

    fun getTeamBId(): String? {
        return teamBId
    }

    fun setTeamBId(teamBId: String?) {
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

    fun getDate(): String? {
        return date
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time
    }

}