package com.e.jannet_stable_code.ui.loginRegister.addChildScreen

import com.e.jannet_stable_code.utils.Utilities

class AddChildScreenValidations(val addChildActivity: AddChildActivity) {

    fun checkValidations(list: ArrayList<AddChildActivity.ChildObject>,flag:Boolean,isEdit:Boolean): Boolean {
        for (i in list.indices) {
            val item = list[i]
            if(flag){
                if (!totalSingleItemValidation(item, i + 1,isEdit))
                    return false
            }else {
                if (!singleItemValidation(item, i + 1))
                    return false
            }
        }

        return true
    }

    private fun totalSingleItemValidation(item: AddChildActivity.ChildObject, int: Int,isEdit:Boolean): Boolean {
        if (!isEdit && item.childImage == "") {
            Utilities.showToast(addChildActivity,"Please select image to continue.")
            return false
        } else if (item.firstName == "") {
            Utilities.showToast(
                addChildActivity,
                "Please enter name to continue."
            )
            return false
        } else if (item.email == "") {
            Utilities.showToast(
                addChildActivity,
                "Please enter email to continue."
            )
            return false
        } else if (!Utilities.isValidEmail(item.email)) {
            Utilities.showToast(
                addChildActivity,
                "Please enter valid email to continue."
            )
            return false
        }
//        else if (!isEdit && item.password == "") {
//            Utilities.showToast(
//                addChildActivity,
//                "Please enter password to continue."
//            )
//            return false
//        }
//        else if (!isEdit && item.password.length < 6) {
//            Utilities.showToast(
//                addChildActivity,
//                "Please enter longer password to continue."
//            )
//            return false
  //      }
    else if (item.birthdate == "") {
            Utilities.showToast(
                addChildActivity, "Please enter birthdate to continue."
            )
            return false
        } else if (item.sportsIds == "") {
            Utilities.showToast(
                addChildActivity,
                "Please select sports to continue."
            )
            return false
        }
        return true
    }

    private fun singleItemValidation(item: AddChildActivity.ChildObject, int: Int): Boolean {
        if (item.childImage == "") {
            Utilities.showToast(
                addChildActivity,
                "Please enter image for child no $int to continue."
            )
            return false
        } else if (item.firstName == "") {
            Utilities.showToast(
                addChildActivity,
                "Please enter name for child no $int to continue."
            )
            return false
        } else if (item.email == "") {
            Utilities.showToast(
                addChildActivity,
                "Please enter email for child no $int to continue."
            )
            return false
        } else if (!Utilities.isValidEmail(item.email)) {
            Utilities.showToast(
                addChildActivity,
                "Please enter valid email for child no $int to continue."
            )
            return false
        }
//        else if (item.password == "") {
//            Utilities.showToast(
//                addChildActivity,
//                "Please enter password for child no $int to continue."
//            )
//            return false
//        } else if (item.password.length < 6) {
//            Utilities.showToast(
//                addChildActivity,
//                "Please enter longer password for child no $int to continue."
//            )
//            return false
//        }
        else if (item.birthdate == "") {
            Utilities.showToast(
                addChildActivity,
                "Please enter birthdate for child no $int to continue."
            )
            return false
        } else if (item.sportsIds == "") {
            Utilities.showToast(
                addChildActivity,
                "Please select sports for child no $int to continue."
            )
            return false
        }
        return true
    }

}