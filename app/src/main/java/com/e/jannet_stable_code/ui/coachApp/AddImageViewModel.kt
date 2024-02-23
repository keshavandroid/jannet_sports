package com.e.jannet_stable_code.ui.coachApp

import android.content.Intent
import android.util.Log
import com.e.jannet_stable_code.retrofit.ControllerInterface
import com.e.jannet_stable_code.retrofit.controller.AddImageController
import com.e.jannet_stable_code.retrofit.controller.EditEventController
import com.e.jannet_stable_code.retrofit.response.BasicResponse
import com.e.jannet_stable_code.ui.coachApp.addEventScreen.EditEventActivity
import com.e.jannet_stable_code.utils.Utilities

class AddImageViewModel(val activity: EditEventActivity) {

    var obj :EditEventActivity.AddImageObject?=null

    fun checkValidData(obj: EditEventActivity.AddImageObject):Boolean {

        val list = ArrayList<String>()
        if (obj.img2 != "") list.add(obj.img2)
        if (obj.img3 != "") list.add(obj.img3)
        if (obj.img4 != "") list.add(obj.img4)
        if (obj.img5 != "") list.add(obj.img5)
        if (obj.img6 != "") list.add(obj.img6)

        obj.img2 = ""
        obj.img3 = ""
        obj.img4 = ""
        obj.img5 = ""
        obj.img6 = ""


        for (i in list.indices){
            if(i==1)obj.img2=list[i]
            if(i==2)obj.img3=list[i]
            if(i==3)obj.img4=list[i]
            if(i==4)obj.img5=list[i]
            if(i==5)obj.img6=list[i]
        }
        obj.imgCount= (list.size-1).toString()


        this.obj=obj
        return true
    }


    fun callAddmageApi() {
        AddImageController(activity,object : ControllerInterface {
            override fun onFail(error: String?) {
                Utilities.showToast(activity,error)
            }

            override fun <T> onSuccess(response: T, method: String) {

                val resp = response as BasicResponse
                val event_id =
                    Log.e("AddEventController", "onSuccess: event id =====" )
                Utilities.showToast(activity,"Event added successfully")
                val intent = Intent(activity, CoachMainActivity::class.java)
                intent.putExtra("eventId",event_id.toString())
                activity.startActivity(intent)

                activity.finish()


            }
        }).callApi(obj!!)
    }


}