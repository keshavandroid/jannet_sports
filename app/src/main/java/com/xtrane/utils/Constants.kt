package com.xtrane.utils

import com.xtrane.retrofit.response.ChildListObject
import com.xtrane.retrofit.response.EventDetailResponse

object Constants {
    //1=coach
    //0=parent
    var userType = 0

    val USER_TYPE = "usertype"

    //user types
    val PARTICIPANT = "participant"
    val COACH = "coach"
    val CHILD = "child"
    val ADULT = "adult"


    //Add child activity
    val CHILD_FROM = "from"
    val EMAIL_VERIFICATION = "emailVerification"
    val EDIT_CHILD = "editChild"
    val ADD_CHILD = "addChild"


    val appOpen = "addChild"

    var eventDetailTop: EventDetailResponse.Result? = null
    var topChildData: ChildListObject? = null
    val COACH_ID = "coach_id"
    val COACH_TOKEN = "coach_token"
    val COACH_TYPE = "user_type"
    val COACH_IMAGE = "coach_image"
    val COACH_Email = "coach_email"
    val COACH_PHONE = "coach_phone"

    //Intent constants
    val LATITUDE = "latitude"
    val LONGITUDE = "longitude"

    var CHILD_ID = "childId"
    var FEES = "fees"

    //Child Edit Data
    var CHILD_FIRSTNAME = "firstName"
    var CHILD_IMAGE = "childImage"

}