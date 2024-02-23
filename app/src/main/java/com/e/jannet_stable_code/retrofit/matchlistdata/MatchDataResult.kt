package com.e.jannet_stable_code.retrofit.matchlistdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MatchDataResult {

    @SerializedName("matchId")
    @Expose
    private var matchId: Int? = null

    @SerializedName("teamAName")
    @Expose
    private var teamAName: String? = null

    @SerializedName("teamBName")
    @Expose
    private var teamBName: String? = null

    @SerializedName("date")
    @Expose
    private var date: String? = null

    @SerializedName("time")
    @Expose
    private var time: String? = null

    @SerializedName("coat")
    @Expose
    private var coat: String? = null


    fun getMatchId(): Int? {
        return matchId
    }

    fun setMatchId(matchId: Int?) {
        this.matchId = matchId
    }

    fun getTeamAName(): String? {
        return teamAName
    }

    fun setTeamAName(teamAName: String?) {
        this.teamAName = teamAName
    }


    fun getTeamBName(): String? {
        return teamBName
    }

    fun setTeamBName(teamBName: String?) {
        this.teamBName = teamBName
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
