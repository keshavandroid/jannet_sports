package com.e.jannet_stable_code.ui.commonApp

import android.os.Bundle
import android.util.Log
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.NotificationAdapter
import com.e.jannet_stable_code.databinding.ActivityMatchListBinding
import com.e.jannet_stable_code.databinding.ActivityNotificationsBinding
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.notifications.NotificationResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.Constants
import com.e.jannet_stable_code.utils.SharedPrefUserData
import com.e.jannet_stable_code.utils.StoreUserData
import com.e.jannet_stable_code.viewinterface.INotificationView

class NotificationsActivity : BaseActivity(),INotificationView {

    lateinit var controller: INotificationController

    var id = ""
    var token = ""

    override fun getController(): IBaseController? {
        return null
    }
    private lateinit var binding: ActivityNotificationsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_notifications)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTopBar()

        val storedata = StoreUserData(this)
        val user_type = storedata.getString(Constants.COACH_TYPE)

        Log.e("USER_TYPE1=", user_type)
        Log.e("USER_TYPE2=", SharedPrefUserData(this).getSavedData().usertype)

        if (user_type.length>0 && user_type.equals(Constants.COACH))
        {
            id = storedata.getString(Constants.COACH_ID)
            token = storedata.getString(Constants.COACH_TOKEN)
        }
        else
        {
            id = SharedPrefUserData(this).getSavedData().id
            token =SharedPrefUserData(this).getSavedData().token
        }

        showLoader()

        controller = NotificationController(this, this)
        controller.callNotificationAPI(id, token)



    }

    private fun setRecyclerview(response: ArrayList<NotificationResult?>) {
        val adapter = NotificationAdapter(this, response)
        binding.rvNotifications.adapter = adapter
        adapter.notifyDataSetChanged()

    }


    private fun setTopBar() {
        binding.topbar.imgBack.setOnClickListener { finish() }
        binding.topbar.txtTitle.text = getString(R.string.txt_notifications)
    }

    override fun onNotificationSuccess(response: ArrayList<NotificationResult?>?) {

        hideLoader()

        Log.e("onNotificationSuccess=", response!!.toString())

        if(!response.isNullOrEmpty()){
            setRecyclerview(response)
        }


    }


    override fun onFail(message: String?, e: Exception?) {
        Log.e("onFail=", message!!)

        showToast(message)
        hideLoader()
    }
}