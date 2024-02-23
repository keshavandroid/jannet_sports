package com.e.jannet_stable_code.retrofit.teamdetaildata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class TeamDetailResult {


    @SerializedName("teamId")
    @Expose
    private var teamId: Int? = null

    @SerializedName("teamName")
    @Expose
    private var teamName: String? = null

    @SerializedName("description")
    @Expose
    private var description: String? = null

    @SerializedName("coachName")
    @Expose
    private var coachName: String? = null

    @SerializedName("sportsName")
    @Expose
    private var sportsName: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("participants")
    @Expose
    private var participants: List<Any?>? = null

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

    fun getCoachName(): String? {
        return coachName
    }

    fun setCoachName(coachName: String?) {
        this.coachName = coachName
    }

    fun getSportsName(): String? {
        return sportsName
    }

    fun setSportsName(sportsName: String?) {
        this.sportsName = sportsName
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getParticipants(): List<Any?>? {
        return participants
    }

    fun setParticipants(participants: List<Any?>?) {
        this.participants = participants
    }

}