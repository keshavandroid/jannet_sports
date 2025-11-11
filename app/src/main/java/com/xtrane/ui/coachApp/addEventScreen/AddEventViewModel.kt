package com.xtrane.ui.coachApp.addEventScreen

import android.content.Intent
import android.util.Log
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.addeventBaseResponse.AddEventBaseResponse
import com.xtrane.retrofit.controller.AddEventCoachController
import com.xtrane.utils.Utilities

class AddEventViewModel(val activity: AddEventActivity) {

    var obj: AddEventActivity.AddEventObject? = null
    fun checkValidData(obj: AddEventActivity.AddEventObject): Boolean {

        if (obj.img1 == "" && obj.img2 == "" && obj.img3 == "" && obj.img4 == "" && obj.img5 == "" && obj.img6 == "") {
            Utilities.showToast(activity, "Select at least one image to continue.")
            return false
        }
//        else if(obj.event_name==""){
//            Utilities.showToast(activity,"Enter name to continue.")
//            return false
//        }
        else if (obj.fees == "") {
            Utilities.showToast(activity, "Enter fees to continue.")
            return false
        } else if (obj.location == "") {
            Utilities.showToast(activity, "Enter location to continue.")
            return false
        } else if (obj.sportTypes == "") {
            Utilities.showToast(activity, "Select sport types to continue.")
            return false
        } else if (obj.date == "") {
            Utilities.showToast(activity, "Enter date to continue.")
            return false
        } else if (obj.noParticipants == "") {
            Utilities.showToast(activity, "Enter number of participants to continue.")
            return false
        }


        val list = ArrayList<String>()
        if (obj.img1 != "") list.add(obj.img1)
        if (obj.img2 != "") list.add(obj.img2)
        if (obj.img3 != "") list.add(obj.img3)
        if (obj.img4 != "") list.add(obj.img4)
        if (obj.img5 != "") list.add(obj.img5)
        if (obj.img6 != "") list.add(obj.img6)

        obj.img1 = ""
        obj.img2 = ""
        obj.img3 = ""
        obj.img4 = ""
        obj.img5 = ""
        obj.img6 = ""

        for (i in list.indices) {
            if (i == 0) obj.img1 = list[i]
            if (i == 1) obj.img2 = list[i]
            if (i == 2) obj.img3 = list[i]
            if (i == 3) obj.img4 = list[i]
            if (i == 4) obj.img5 = list[i]
            if (i == 5) obj.img6 = list[i]
        }
        obj.imgCount = (list.size - 1).toString()

        this.obj = obj
        return true
    }

    fun callAddEventApi() {
        AddEventCoachController(activity, object : ControllerInterface {
            override fun onFail(error: String?) {
                Utilities.showToast(activity, error)
            }

            override fun <T> onSuccess(response: T, method: String) {

                val resp = response as AddEventBaseResponse
                val event_id = resp.getResult()?.get(0)?.getEventId()
                Log.e("AddEventController", "onSuccess: event id =====$event_id")
                Utilities.showToast(activity, "Event added successfully")

                val intent = Intent(activity, AddTeamsActivity::class.java)
                intent.putExtra("eid", event_id.toString())
                activity.startActivity(intent)

                activity.finish()
            }
        }).callApi(obj!!)
    }

}