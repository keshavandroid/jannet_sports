package com.e.jannet_stable_code.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.e.jannet_stable_code.retrofit.addeventBaseResponse.AddEventBaseResponse
import com.e.jannet_stable_code.retrofit.addeventBaseResponse.AddEventResult
import com.e.jannet_stable_code.retrofit.response.*
import java.util.*
import kotlin.collections.ArrayList

class SharedPrefUserData(context: Activity) {
    private var pref: SharedPreferences? = null
    private var activity: Activity? = context
    private var APP_KEY: String? = null
    private val TAG = javaClass.simpleName

    //User data constants
    private val ID = "id"
    private val NAME = "name"
    private val FIRSTNAME = "firstname"
    private val LASTNAME = "lastname"
    private val GENDER = "gender"
    private val BIRTHDATE = "birthdate"
    private val EMAIL = "email"
    private val PASSWORD = "password"
    private val CONTACTNO = "contactNo"
    private val USERTYPE = "userType"
    private val IMAGE = "image"
    private val USERTOKEN = "userToken"
    private val LOCATION = "location"
    private val REGISTER_STEP = "registerStep"
    private val SPORTS_ID = "sportId"
    private val SPORTS_NAME = "sportName"
    private val SPORTS_COUNT = "sportCount"
    private val IMAGE_ID = "image_id"
    private val EVENT_ID = "event_id"

    init {
        APP_KEY = context.packageName.replace("\\.".toRegex(), "_").toLowerCase(Locale.getDefault())
        Log.d(TAG, "StoreUserData: $APP_KEY")
        pref = activity?.getSharedPreferences(
            APP_KEY,
            Context.MODE_PRIVATE
        )
    }

    private fun saveString(tag: String?, dataStr: String?) {
        try {
            pref!!.edit().putString(tag, dataStr).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getString(tag: String?): String {
        try {
            if (pref!!.getString(tag, "") != null && !pref!!.getString(tag, "").equals("null"))
                return pref!!.getString(tag, "")!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun clearAllData() {
        pref?.edit()?.clear()?.apply()
    }

    fun getSavedData(): SavedData {
        val data = SavedData()

        data.id = getString(ID)
        data.firstname = getString(FIRSTNAME)
        data.lastname = getString(LASTNAME)
        data.gender = getString(GENDER)
        data.birthdate = getString(BIRTHDATE)
        data.email = getString(EMAIL)
        data.password = getString(PASSWORD)
        data.contactno = getString(CONTACTNO)
        data.usertype = getString(USERTYPE)
        data.image = getString(IMAGE)
        data.token = getString(USERTOKEN)
        data.registerStep = getString(REGISTER_STEP)
        data.location = getString(LOCATION)

        data.sportList = ArrayList()

        try {
            val count = getString(SPORTS_COUNT).toInt()

            for( i in 0 until count){
                val item = Sports()
                item.sportId=getString(SPORTS_ID)
                item.sportName=getString(SPORTS_NAME)

                data.sportList!!.add(item)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return data
    }

    fun setRegisterStep(s: String) {
        saveString(REGISTER_STEP, s)
    }

    fun saveLoginResp(data: LoginResponse.Result) {
        saveString(ID, data.id.toString())
        saveString(FIRSTNAME, data.firstname)
        saveString(LASTNAME, data.lastname)
        saveString(GENDER, data.gender)
        saveString(BIRTHDATE, data.birthdate)
        saveString(EMAIL, data.email)
        saveString(PASSWORD, data.password)
        saveString(CONTACTNO, data.contactNo)
        saveString(USERTYPE, data.userType)
        saveString(IMAGE, data.image)
        saveString(USERTOKEN, data.userToken)
        saveString(REGISTER_STEP, data.status)
    }

    fun saveCoachLoginResp(userType: String) {
        saveString(USERTYPE, userType)
    }

    fun saveProfileResp(result: GetProfileParentApiResponse.Result?) {
        saveString(ID, result!!.id.toString())
        saveString(NAME, result.name)
        saveString(FIRSTNAME, result.firstname)
        saveString(LASTNAME, result.lastname)
        saveString(GENDER, result.gender)
        saveString(BIRTHDATE, result.birthdate)
        saveString(EMAIL, result.email)
        saveString(PASSWORD, result.password)
        saveString(CONTACTNO, result.contactNo)
        saveString(USERTYPE, result.userType)
        saveString(IMAGE, result.image)
        saveString(USERTOKEN, result.userToken)

        try {
            for (i in result.sports!!.indices) {
                saveString(SPORTS_ID + i, result.sports[i].id.toString())
                saveString(SPORTS_NAME + i, result.sports[i].sportsName)
            }
            saveString(SPORTS_COUNT, result.sports.size.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            saveString(SPORTS_COUNT, "0")
        }
    }
    fun saveProfileResp(result: GetProfileCoachApiResponse.Result?) {
        saveString(ID, result!!.id.toString())
        saveString(NAME, result.name)
        saveString(FIRSTNAME, "")
        saveString(LASTNAME, "")
        saveString(GENDER, result.gender)
        saveString(BIRTHDATE, result.birthdate)
        saveString(EMAIL, result.email)
        saveString(PASSWORD, result.password)
        saveString(CONTACTNO, result.contactNo)
        saveString(USERTYPE, result.userType)
        saveString(IMAGE, result.image)
        saveString(USERTOKEN, result.userToken)
        saveString(LOCATION, result.location)

        try {
            for (i in result.sports!!.indices) {
                saveString(SPORTS_ID + i, result.sports!![i].id.toString())
                saveString(SPORTS_NAME + i, result.sports!![i].sportsName)
            }
            saveString(SPORTS_COUNT, result.sports!!.size.toString())

        } catch (e: Exception) {
            e.printStackTrace()
            saveString(SPORTS_COUNT, "0")
        }
    }

    fun saveAddEventData(response:AddEventResult){

        saveString(EVENT_ID, response.getEventId().toString())

        saveString(IMAGE_ID,response.getId().toString())


    }

    fun saveRegisterCoach(result: RegisterParentResponse.Result?) {
        saveString(ID, result!!.id.toString())
        saveString(FIRSTNAME, result.firstname)
        saveString(LASTNAME, result.lastname)
        saveString(GENDER, result.gender)
        saveString(BIRTHDATE, result.birthdate)
        saveString(EMAIL, result.email)
        saveString(PASSWORD, result.password)
        saveString(CONTACTNO, result.contactNo)
        saveString(USERTYPE, result.userType)
        saveString(IMAGE, result.image)
        saveString(USERTOKEN, result.userToken)
    }

    fun saveRegisterCoach(result: CoachRegisterResponse.Result?) {
        saveString(ID, result!!.id.toString())
        saveString(FIRSTNAME, result.name)
        saveString(GENDER, result.gender)
        saveString(BIRTHDATE, result.birthdate)
        saveString(EMAIL, result.email)
        saveString(PASSWORD, result.password)
        saveString(CONTACTNO, result.contactNo)
        saveString(USERTYPE, result.userType)
        saveString(IMAGE, result.image)
        saveString(USERTOKEN, result.userToken)
        saveString(LOCATION, result.location)
    }




    fun saveProfileResp(response: CoachRegisterResponse) {
        saveString(NAME, response.getResult()!!.name)
        saveString(GENDER, response.getResult()!!.gender)
        saveString(BIRTHDATE, response.getResult()!!.birthdate)
        saveString(EMAIL, response.getResult()!!.email)
        saveString(PASSWORD, response.getResult()!!.password)
        saveString(CONTACTNO, response.getResult()!!.contactNo)
        saveString(IMAGE, response.getResult()!!.image)
        saveString(USERTOKEN, response.getResult()!!.userToken)
        saveString(LOCATION, response.getResult()!!.location)
    }



    class SavedData {
        var id = ""
        var firstname = ""
        var lastname = ""
        var gender = ""
        var birthdate = ""
        var email = ""
        var password = ""
        var contactno = ""
        var usertype = ""
        var image = ""
        var token = ""
        var registerStep = ""
        var location = ""

        var sportList: ArrayList<Sports>? = null
    }

    class Sports {
        var sportId = ""
        var sportName = ""
    }
}