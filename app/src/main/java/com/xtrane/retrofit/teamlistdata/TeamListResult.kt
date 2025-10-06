package com.xtrane.retrofit.teamlistdata

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class TeamListResult {

    @SerializedName("teamId")
    @Expose
    private var teamId: Int? = null

    @SerializedName("coach_id")
    @Expose
    private var coach_id: Int? = null

    fun setCoachID(coach_id: Int?) {
        this.coach_id = coach_id
    }
    fun getCoachID(): Int? {
        return coach_id
    }


    @SerializedName("teamName")
    @Expose
    private var teamName: String? = null

    @SerializedName("description")
    @Expose
    private var description: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

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

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }


    override fun toString(): String {
        return "$teamName"
    }

}