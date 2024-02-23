package com.e.jannet_stable_code.retrofit.bookinglistdata

import com.e.jannet_stable_code.retrofit.matchlistdata.MatchDataResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BookTicketDetailResult {


    @SerializedName("userName")
    @Expose
    private var userName: String? = null

    @SerializedName("totalTickets")
    @Expose
    private var totalTickets: String? = null

    @SerializedName("team_a_name")
    @Expose
    private var team_a_name: String? = null

    @SerializedName("team_b_name")
    @Expose
    private var team_b_name: String? = null

    @SerializedName("date")
    @Expose
    private var date: String? = null

    @SerializedName("time")
    @Expose
    private var time: String? = null

    @SerializedName("coat")
    @Expose
    private var coat: String? = null

    @SerializedName("amount")
    @Expose
    private var amount: String? = null

    @SerializedName("MatchData")
    @Expose
    private var matchData: List<MatchDataResult?>? = null


    fun getuserName(): String? {
        return userName
    }

    fun setBookId(userName: String?) {
        this.userName = userName
    }
  fun getMatchData(): List<MatchDataResult?>?? {
        return matchData
    }

    fun setMatchData(matchData: List<MatchDataResult?>??) {
        this.matchData = matchData
    }

    fun gettotalTickets(): String? {
        return totalTickets
    }

    fun settotalTickets(totalTickets: String?) {
        this.totalTickets = totalTickets
    }

    fun getteam_a_name(): String? {
        return team_a_name
    }

    fun setteam_a_name(team_a_name: String?) {
        this.team_a_name = team_a_name
    }

    fun getteam_b_name(): String? {
        return team_b_name
    }

    fun setteam_b_name(team_b_name: String?) {
        this.team_b_name = team_b_name
    }

    fun getdate(): String? {
        return date
    }

    fun setdate(date: String?) {
        this.date = date
    }

    fun gettime(): String? {
        return time
    }

    fun settime(time: String?) {
        this.date = time
    }

    fun getcoat(): String? {
        return coat
    }

    fun setcoat(coat: String?) {
        this.coat = coat
    }

    fun getamount(): String? {
        return amount
    }

    fun setamount(amount: String?) {
        this.amount = amount
    }


}