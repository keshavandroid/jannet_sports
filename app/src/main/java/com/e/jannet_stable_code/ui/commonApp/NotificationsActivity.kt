package com.e.jannet_stable_code.ui.commonApp

import android.os.Bundle
import com.e.jannet_stable_code.R
import com.e.jannet_stable_code.adapter.NotificationAdapter
import com.e.jannet_stable_code.retrofit.controller.*
import com.e.jannet_stable_code.retrofit.notifications.NotificationResult
import com.e.jannet_stable_code.ui.BaseActivity
import com.e.jannet_stable_code.utils.SharedPrefUserData
import kotlinx.android.synthetic.main.topbar_layout.*
import com.e.jannet_stable_code.viewinterface.INotificationView
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity : BaseActivity(),INotificationView {

    lateinit var controller: INotificationController

    var id = ""
    var token = ""

    override fun getController(): IBaseController? {
        return null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setTopBar()


        controller = NotificationController(this, this)

        showLoader()
        id = SharedPrefUserData(this).getSavedData().id
        token = SharedPrefUserData(this).getSavedData().token
        controller.callNotificationAPI(id, token)



    }

    private fun setRecyclerview(response: ArrayList<NotificationResult?>) {
        val adapter = NotificationAdapter(this, response)
        rvNotifications.adapter = adapter
        adapter.notifyDataSetChanged()

    }


    private fun setTopBar() {
        imgBack.setOnClickListener { finish() }
        txtTitle.text = getString(R.string.txt_notifications)
    }

    override fun onNotificationSuccess(response: ArrayList<NotificationResult?>?) {
        hideLoader()

        if(!response.isNullOrEmpty()){
            setRecyclerview(response)
        }


    }


    override fun onFail(message: String?, e: Exception?) {
        showToast(message)
        hideLoader()
    }
}