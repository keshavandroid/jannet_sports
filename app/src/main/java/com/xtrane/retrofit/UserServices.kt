package com.xtrane.retrofit

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UserServices {

    //Coach

    @POST("coachLogin")
    @FormUrlEncoded
    fun coachLoginApi(
        @Field(encoded = true, value = "email") email: String?,
        @Field(encoded = true, value = "password") password: String?,
    ): Call<ResponseBody?>?


    @POST("login")
    @FormUrlEncoded
    fun loginApi(
        @Field("email") email: String?, @Field("password") registerType: String?,
        @Field("userType") userType: String?,
    ): Call<ResponseBody?>?

    @POST("saveSportList")
    @FormUrlEncoded
    fun saveSportListApi(
        @Field("id") id: String?,
        @Field("token") token: String?,
        @Field("sportsId") sportsId: String?,
    ): Call<ResponseBody?>?

    @POST("changePassword")
    @FormUrlEncoded
    fun changePasswordApi(
        @Field("id") id: String?,
        @Field("token") token: String?,
        @Field("oldpassword") oldpassword: String?,
        @Field("newpassword") newpassword: String?,
    ): Call<ResponseBody?>?

    @POST("deleteChild")
    @FormUrlEncoded
    fun deleteChildApi(
        @Field("id") id: String?,
        @Field("token") token: String?,
        @Field("childId") childId: String?,
    ): Call<ResponseBody?>?

    @GET("getSportList")
    fun getSportList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @GET("getPurchaseHistory")
    fun getPurchaseHistory(
        @Query("userId") id: String?, @Query("userToken") token: String?,
    ): Call<ResponseBody?>?

    @GET("parentHomeFilter")
    fun parentHomeFilter(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("location_id") location_id: String?,
        @Query("sportsId") sportsId: String?,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?,
        @Query("month") month: String?,
        @Query("radious") radius: String?,
        ): Call<ResponseBody?>?

    @GET("eventDetail")
    fun eventDetail(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") eventid: String?,
    ): Call<ResponseBody?>?

    @GET("parentHomeFilter")
    fun eventList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("startDate") startDate: String?,
        ): Call<ResponseBody?>?

    @GET("getParentMatch")
    fun getParentMatch(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @GET("coachEventList")
    fun coachEventList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("coach_id") coach_id: String?,
        @Query("type") type: String?,
    ): Call<ResponseBody?>?

    @GET("getArchiveCoachEventList")
    fun getArchiveCoachEventList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("isarchive") isarchive: String?,
    ): Call<ResponseBody?>?

    //second tab API in Participant
    @GET("getRegisterList")
    fun getRegisterList(
        @Query("id") id: String?,
        @Query("token") token: String?,
    ): Call<ResponseBody?>?

    //second tab MatchHistory

    @GET("getUserMatchHistory")
    fun getUserMatchHistory(
        @Query("id") id: String?,
        @Query("token") token: String?,
    ): Call<ResponseBody?>?


    //Event Register Request List
    @GET("getAllEventReq")
    fun getAllEventReq(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("status") status: String?,
    ): Call<ResponseBody?>?

    @POST("acceptRejectEvent")
    @FormUrlEncoded
    fun acceptRejectEvent(
        @Field("id") id: String?,
        @Field("token") token: String?,
        @Field("child_id") child_id: String?,
        @Field("event_id") event_id: String?,
        @Field("status") status: String?,
    ): Call<ResponseBody?>?

    @POST("acceptRejectTicket")
    @FormUrlEncoded
    fun acceptRejectTicket(
        @Field("id") id: String?,
        @Field("token") token: String?,
        @Field("child_id") child_id: String?,
        @Field("event_id") event_id: String?,
        @Field("status") status: String?,
        @Field("match_id") match_id: String?,
    ): Call<ResponseBody?>?

    //Ticket Booking Request List
    @GET("getAllTicketReq")
    fun getAllTicketReq(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("status") status: String?,
    ): Call<ResponseBody?>?

    @GET("childGroupList")
    fun childGroupList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @GET("getCoachSportList")
    fun getCoachSportList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @GET("getProfile")
    fun getProfileApi(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("userType") userType: String?,
    ): Call<ResponseBody?>?

    @POST("sendPhoneEmailOtp")
    @FormUrlEncoded
    fun sendPhoneEmailOtpApi(
        @Field("email") email: String?,
        @Field("phoneNo") phonenumber: String?,
        @Field("id") id: String?,
    ): Call<ResponseBody?>?

    @POST("checkPhoneEmailOtp")
    @FormUrlEncoded
    fun checkPhoneEmailOtpApi(
        @Field("emailOtp") emailOtp: String?,
        @Field("phoneOtp") phoneOtp: String?,
        @Field("id") id: String?,
    ): Call<ResponseBody?>?

    @GET("privacyPolicy")
    fun privacyPolicyApi(): Call<ResponseBody?>?

    @GET("Games/{year}")
    fun gameListByYearNBA(
        @Path("year") year: String?, @Query("key") key: String?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("register")
    fun registerParentApi(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("middleName") middleName: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("contactNo") phoneNo: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("userType") usertype: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("bookEvent")
    fun bookEventApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("child_id") child_id: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("event_id") event_id: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("bookEvent")
    fun bookChildEvent(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("child_id") child_id: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("event_id") event_id: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("bookEventRequest")
    fun bookEventRequest(
        @Field("id") id: String,
        @Field("token") token: String,
        @Field("child_id") child_id: String,
        @Field("event_id") event_id: String,
    ): Call<ResponseBody?>?


    @Multipart
    @POST("editCoachProfile")
    fun editCoachProfileApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("sports") sports: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("email") email: RequestBody,
        @Part("contactNo") contactNo: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("editProfile")
    fun editParentProfileApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("name") name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("email") email: RequestBody,
        @Part("contactNo") contactNo: RequestBody,
        @Part("userType") usertype: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("middleName") middleName: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("addEvent")
    fun addEventApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("event_name") event_name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("location_id") location_id: RequestBody,
        @Part("sports_id") sports_id: RequestBody,
        @Part("coach_id") coach_id: RequestBody,
        @Part("date") date: RequestBody,
        @Part("participants") participants: RequestBody,
        @Part("gender_applicable") gender_applicable: RequestBody,
        @Part("grade_id") grade_id: RequestBody,
        @Part("min_age") min_age: RequestBody,
        @Part("max_age") max_age: RequestBody,
        @Part("min_grade") min_grade: RequestBody,
        @Part("max_grade") max_grade: RequestBody,
        @Part("matchType") matchType: RequestBody,
        @Part("time") time: RequestBody,
        @Part("image") image: RequestBody,
        @Part mainimage: MultipartBody.Part?,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image4: MultipartBody.Part?,
        @Part image5: MultipartBody.Part?,
    ): Call<ResponseBody?>?
//

    @Multipart
    @POST("registerCoach")
    fun registerCoachApi(
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("contactNo") contactNo: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("registerChildUser")
    fun registerChildUser(
        @Part("allow_book") allowBook: RequestBody,
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("password") password: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("middleName") middleName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("jurseyName") jurseyName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("sports") sports: RequestBody,
        @Part("gradeId") gradeId: RequestBody,
        @Part("childGender") childGender: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    //        @Part("password") password: RequestBody,
    @Multipart
    @POST("editChildProfile")
    fun editChildProfile(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("childid") childid: RequestBody,
        @Part("password") password: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("middleName") middleName: RequestBody,
        @Part("jurseyName") jurseyName: RequestBody,
        @Part("gradeId") gradeId: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("email") email: RequestBody,
        @Part("sports") sports: RequestBody,
        @Part("childGender") childGender: RequestBody,
        @Part childImage: MultipartBody.Part?,
    ): Call<ResponseBody?>?

    @GET("childInfo")
    fun getChildInfo(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("childIds") childIds: String?,
    ): Call<ResponseBody?>?


    @Multipart
    @POST("addTeam")
    fun addTeamApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("event_id") event_id: RequestBody,
        @Part("coach_id") coach_id: RequestBody,
        @Part("teamName") teamName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?


    @GET("getTeam")
    fun getTeamList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?


    @POST("addMatch")
    fun addMatch(@Body jsonObject: JsonObject): Call<ResponseBody?>?

    @GET("getMatch")
    fun getMatchList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?

    @GET("getNotificationList")
    fun getNotificationList(
        @Query("id") id: String?,
        @Query("token") token: String?,
    ): Call<ResponseBody?>?


    @POST("androidDeviceRegister")
    fun deviceRegisterNoti(@Body jsonObject: JsonObject): Call<ResponseBody?>?


    @POST("addMatchPrice")
    fun addMatchPrice(

        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("matchId1") matchId1: RequestBody,
        @Part("matchPrice1") matchPrice1: RequestBody,
        @Part("count") count: RequestBody,
    ): Call<ResponseBody?>?


    @POST("forgotPassword")
    fun forgotPassword(@Body jsonObject: JsonObject): Call<ResponseBody?>?

    @GET("staticPage")
    fun getStaticPage(
        @Query("pageId") pageId: String,
        @Query("id") id: String?,
        @Query("token") token: String?,
    ): Call<ResponseBody?>?


    @GET("getLocation")
    fun getLocation(
        @Query("id") id: String?,
        @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @POST("deleteTeam")
    fun deleteTeam(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("teamId") teamId: String?,

        ): Call<ResponseBody?>?

    @POST("deleteMatch")
    fun deleteMatch(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("matchId") matchId: String?,

        ): Call<ResponseBody?>?

    @POST("editMatch")
    fun editMatch(@Body jsonObject: JsonObject): Call<ResponseBody?>?

    @Multipart
    @POST("editTeam")
    fun editTeam(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("teamId") teamId: RequestBody,
        @Part("teamName") teamName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?


    @POST("deleteEvent")
    fun deleteEvent(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("eventId") eventId: String?,
    ): Call<ResponseBody?>?


    @Multipart
    @POST("editEvent")
    fun editEventApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("event_id") event_id: RequestBody,
        @Part("event_name") event_name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("location_id") location_id: RequestBody,
        @Part("sports_id") sports_id: RequestBody,
        @Part("coach_id") coach_id: RequestBody,
        @Part("date") date: RequestBody,
        @Part("participants") participants: RequestBody,
        @Part("gender_applicable") gender_applicable: RequestBody,
        @Part("grade_id") grade_id: RequestBody,
        @Part("min_age") min_age: RequestBody,
        @Part("max_age") max_age: RequestBody,
        @Part("imageCount") imageCount: RequestBody,
        @Part("imageId1") imageId1: RequestBody,
        @Part("imageId2") imageId2: RequestBody,
        @Part("imageId3") imageId3: RequestBody,
        @Part("imageId4") imageId4: RequestBody,
        @Part("imageId5") imageId5: RequestBody,
        @Part("imageId6") imageId6: RequestBody,

        @Part mainimage: MultipartBody.Part?,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image4: MultipartBody.Part?,
        @Part image5: MultipartBody.Part?,
    ): Call<ResponseBody?>?
//        @Body images: List<ImageListObject>?


    @Multipart
    @POST("addEventImages")
    fun addImage(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("event_id") event_id: RequestBody,
        @Part("count") count: RequestBody,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image4: MultipartBody.Part?,
        @Part image5: MultipartBody.Part?,
    ): Call<ResponseBody?>?


    @GET("getTeamDetail")
    fun getTeamDetail(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("team_id") team_id: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?


    @GET("getNonTeamMember")
    fun getNonTeamParticipants(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?

    @GET("getBookingList")
    fun getBookingList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?


    @GET("getBookingDetail")
    fun getBookingDetails(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("book_id") book_id: String?,
    ): Call<ResponseBody?>?


    @GET("getTicket")
    fun getTickets(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?


    @GET("getCoachSportList")
    fun getCoachSportList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("coach_id") coach_id: String?,
    ): Call<ResponseBody?>?


    @GET("getMainTeam")
    fun mainTeamList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?


    @Multipart
    @POST("addMainTeam")
    fun addMainTeamApi(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("coach_id") coach_id: RequestBody,
        @Part("teamName") teamName: RequestBody,
        @Part("description") description: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("sports_id") sports_id: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?


    @GET("getMainTeamDetail")
    fun mainTeamDetail(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("team_id") team_id: String?,
    ): Call<ResponseBody?>?


    @GET("getGrade")
    fun getGradeList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?


    @POST("addLocation")
    fun addNewLocation(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("address") address: String?,
        @Query("latitude") latitude: String?,
        @Query("longitude") longitude: String?,
        @Query("coat") coat: String?,
    ): Call<ResponseBody?>?

    @POST("addEventReport")
    fun addEventReport(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("eventId") address: String?,
        @Query("reportMessage") message: String?
    ): Call<ResponseBody?>?

    @POST("addRescheduleEvent")
    fun addRescheduleEvent(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("eventId") eventId: String?,
        @Query("date") date: String?,
        @Query("time") time: String?
    ): Call<ResponseBody?>?
    @GET("filterHome")
    fun getCoachFilter(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("location_id") location_id: String?,
        @Query("sportsId") sportsId: String?,
    ): Call<ResponseBody?>?


    @GET("getCoachList")
    fun getCoachList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @GET("getCoachListSportsType")
    fun getCoachListSportsType(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("sportsType") sportsType: String?,
    ): Call<ResponseBody?>?


    @GET("getCoachTeam")
    fun getCoachTeamList(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("coach_id") coach_id: String?,
    ): Call<ResponseBody?>?

    @GET("getAgeRange")
    fun getMiMaxAge(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?


    @POST("bookParentTicket")
    fun bookTicketParent(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
        @Query("fees") fees: String?,
        @Query("total_ticket") total_ticket: String?,
        @Query("match_id") match_id: String?,
        @Query("name") name: String?,
        @Query("contactNo") contactNo: String?,
    ): Call<ResponseBody?>?

    @Multipart
    @POST("joinMainTeam")
    fun joinTeamFromParent(
        @Part("id") id: RequestBody,
        @Part("token") token: RequestBody,
        @Part("coach_id") coach_id: RequestBody,
        @Part("team_id") team_id: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<ResponseBody?>?


    @GET("getEventPrice")
    fun getEventRegisterPrice(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?

    @GET("getJoinTeamPrice")
    fun getJoinTeamPrice(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("coach_id") coach_id: String?,
    ): Call<ResponseBody?>?

    @GET("getBookPriceList")
    fun getBookTicketPrice(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?

    @GET("getJoinTeam")
    fun getJoinTeamList(
        @Query("id") id: String?, @Query("token") token: String?,
    ): Call<ResponseBody?>?

    @GET("bookTicketDetail")
    fun bookTicketDetail(
        @Query("id") id: String?,
        @Query("token") token: String?,
        @Query("event_id") event_id: String?,
    ): Call<ResponseBody?>?

    @POST("createPaymentIntent")
    @FormUrlEncoded
    fun createPaymentIntentApi(
        @Field("amount") amount: String?,
        @Field("currency") currency: String?,
        @Field("eventId") eventId: String?,
        @Field("userId") userId: String?,
        @Field("bookType") bookType: String?,
    ): Call<ResponseBody?>?

    /*@POST("createPaymentIntent")
    @FormUrlEncoded
    fun createPaymentIntentApi(
        @Field("amount") amount: String?,
        @Field("currency") currency: String?
    ): Call<ResponseBody?>?*/




}




