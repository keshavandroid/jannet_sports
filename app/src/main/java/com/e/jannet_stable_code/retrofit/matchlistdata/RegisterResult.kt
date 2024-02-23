package com.e.jannet_stable_code.retrofit.matchlistdata

import com.e.jannet_stable_code.retrofit.coacheventlistdata.CoachEventListResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResult {

    @SerializedName("eventData")
    @Expose
    private var eventData: ArrayList<CoachEventListResult?>? = null

    @SerializedName("ticketData")
    @Expose
    private var ticketData: ArrayList<CoachEventListResult?>? = null

    fun getEventData(): ArrayList<CoachEventListResult?>? {
        return eventData
    }

    fun setEventData(eventData: ArrayList<CoachEventListResult?>?) {
        this.eventData = eventData
    }

    fun getTicketData(): ArrayList<CoachEventListResult?>? {
        return ticketData
    }

    fun setTicketData(ticketData: ArrayList<CoachEventListResult?>?) {
        this.ticketData = ticketData
    }

}