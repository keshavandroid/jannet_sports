package com.e.jannet_stable_code.viewinterface

import com.e.jannet_stable_code.retrofit.notifications.NotificationResult

interface INotificationView:IBaseView {

    fun onNotificationSuccess(response:ArrayList<NotificationResult?>?)

}