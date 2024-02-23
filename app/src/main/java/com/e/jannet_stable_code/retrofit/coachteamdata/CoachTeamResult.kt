package com.e.jannet_stable_code.retrofit.coachteamdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CoachTeamResult {

    @SerializedName("teamId")
    @Expose
    private var teamId: Int? = null

    @SerializedName("teamName")
    @Expose
    private var teamName: String? = null

    @SerializedName("description")
    @Expose
    private var description: String? = null

    @SerializedName("fees")
    @Expose
    private var fees: Any? = null

    @SerializedName("sportsType")
    @Expose
    private var sportsType: String? = null

    @SerializedName("isJoin")
    @Expose
    private var isJoin: Int? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("coachName")
    @Expose
    private var coachName: String? = null

    fun getTeamId(): Int? {
        return teamId
    }

    fun setTeamId(teamId: Int?) {
        this.teamId = teamId
    }

    fun getTeamName(): String? {
        return teamName
    }

    fun setTeamName(teamName: String?) {
        this.teamName = teamName
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun getFees(): Any? {
        return fees
    }

    fun setFees(fees: Any?) {
        this.fees = fees
    }

    fun getSportsType(): String? {
        return sportsType
    }

    fun setSportsType(sportsType: String?) {
        this.sportsType = sportsType
    }

    fun getIsJoin(): Int? {
        return isJoin
    }

    fun setIsJoin(isJoin: Int?) {
        this.isJoin = isJoin
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getCoachName(): String? {
        return coachName
    }

    fun setCoachName(coachName: String?) {
        this.coachName = coachName
    }
}