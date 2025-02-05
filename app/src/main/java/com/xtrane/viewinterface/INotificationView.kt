package com.xtrane.viewinterface

import com.xtrane.retrofit.notifications.NotificationResult

interface INotificationView:IBaseView {

    fun onNotificationSuccess(response:ArrayList<NotificationResult?>?)

}