package com.xtrane.ui.coachApp.coachUpdateProfileScreen

import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.UpdateCoachUserController
import com.xtrane.utils.Utilities

class CoachUpdateProfileModel (val activity: CoachUpdateProfileActivity){
    var image:String=""
    var fname:String=""
    var email:String=""
    var phNo:String=""
    var bdate:String=""
    var gender:String=""
    var location:String=""
    var sportsIds:String=""

    fun checkValidData(image:String,fname:String,email:String,phNo:String,bdate:String,gender:String,location:String,sportsIds:String):Boolean{
//        if(fname.trim()==""){
//            Utilities.showToast(activity,"Please enter first name to continue.")
//            return false
//        }else if(email.trim()==""){
//            Utilities.showToast(activity,"Please enter email to continue.")
//            return false
//        }else if(!Utilities.isValidEmail(email.trim())){
//            Utilities.showToast(activity,"Please enter a valid email to continue.")
//            return false
//        }else if(phNo.trim()==""){
//            Utilities.showToast(activity,"Please enter phone number to continue.")
//            return false
//        }else if(phNo.trim().length<6){
//            Utilities.showToast(activity,"Please enter a valid phone number to continue.")
//            return false
//        }else if(bdate.trim()==""){
//            Utilities.showToast(activity,"Please enter birthdate to continue.")
//            return false
//        }else if(gender.trim()==""){
//            Utilities.showToast(activity,"Please select gender to continue.")
//            return false
//        }
////        else if(location.trim()==""){
////            Utilities.showToast(activity,"Please enter location to continue.")
////            return false
////        }
//        else if(sportsIds.trim()==""){
//            Utilities.showToast(activity,"Please select sports to continue.")
//            return false
//        }

        this.image=image
        this.fname=fname
        this.email=email
        this.phNo=phNo
        this.bdate=bdate
        if(gender=="1"){
            this.gender="m"
        }else if(gender=="2"){
            this.gender="f"
        }
        this.location=location
        this.sportsIds=sportsIds

        return true
    }

    fun updateCoachDetails() {
        UpdateCoachUserController(activity,object:ControllerInterface{
            override fun onFail(error: String?) {
                Utilities.showToast(activity,error)
            }

            override fun <T> onSuccess(response: T, method: String) {
                Utilities.showToast(activity,"Profile updated successfully.")

            }
        }).callApi(image,fname,email,phNo,bdate,gender,location,sportsIds)
    }
}