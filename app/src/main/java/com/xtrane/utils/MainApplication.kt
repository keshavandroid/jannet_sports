package com.xtrane.utils

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.FirebaseApp
import com.stripe.android.PaymentConfiguration


class MainApplication:Application(), LifecycleObserver,
    Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "MainApplication"
        private var instance: MainApplication? = null
        var onAppForegrounded = false

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }

        fun onAppForground() : Boolean {
            return onAppForegrounded
        }
    }

    public var isAppOpened=0
    private var sInstance: MainApplication? = null

    public fun getAppContext(): MainApplication? {
        return sInstance
    }

    /*fun getInstance(): MainApplication? {
        synchronized(MainApplication::class.java) {
            onAppForegrounded = true
            return MainApplication.instance
        }
    }*/

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
        sInstance = this
        instance = this

        FirebaseApp.initializeApp(this)

        // Initialize Firebase notifications
        val notificationManager = FirebaseNotificationManager(this)
        notificationManager.initializeFirebaseNotifications()
        createNotificationChannel()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_YourPublicKeyHere" // Replace with your publishable key
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        onAppForegrounded = true
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        onAppForegrounded = true
    }

    override fun onActivityStarted(activity: Activity) {
        onAppForegrounded = true
    }

    override fun onActivityResumed(activity: Activity) {
        onAppForegrounded = true
    }

    override fun onActivityPaused(activity: Activity) {
        onAppForegrounded = false
    }

    override fun onActivityStopped(activity: Activity) {
        onAppForegrounded = false
    }

    override fun onTrimMemory(level: Int) {
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            onAppForegrounded = false
        }
        super.onTrimMemory(level)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        onAppForegrounded = true
    }

    override fun onActivityDestroyed(activity: Activity) {
        onAppForegrounded = false
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel_id", // same ID as manifest
                "General Notifications", // channel name for settings
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "This channel is used for general notifications"

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}