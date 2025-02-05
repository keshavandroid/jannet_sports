package com.xtrane.ui.loginRegister.addChildScreen

import android.app.Activity
import com.xtrane.retrofit.ControllerInterface
import com.xtrane.retrofit.controller.AddChildToParentController
import com.xtrane.utils.Utilities

class SaveChildsWithApi(
    val childList: ArrayList<AddChildActivity.ChildObject>,
    val activity: Activity,
    val myinterface: OnSaveChildsWithApi
) {
    var currentPos = 0

    init {
        try {
            callSaveApi(childList[0])
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callSaveApi(childObject: AddChildActivity.ChildObject) {
        AddChildToParentController(activity, object : ControllerInterface {
            override fun onFail(error: String?) {
                Utilities.showToast(activity, error)
                myinterface.onFail()
            }

            override fun <T> onSuccess(response: T, method: String) {
                try {

                    if (method == "AddChildSuccess") {
                        currentPos += 1
                        if (childList.size == currentPos) {
                            myinterface.onSuccess()
                        } else callSaveApi(childList[currentPos])
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }).callApi(childObject)
    }


    interface OnSaveChildsWithApi {
        fun onSuccess()
        fun onFail()
    }
}