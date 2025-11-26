package com.xtrane.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.xtrane.R

class FirebaseNotificationHelper(private val context: Context) {

    companion object {
        private const val TAG = "FirebaseNotificationHelper"
        const val CHANNEL_ID = "com.xtrane.notifications"
        const val CHANNEL_NAME = "Xtrane Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications for events, updates, and reminders"
    }

    /**
     * Get Firebase Cloud Messaging token
     */
    @SuppressLint("LongLogTag")
    fun getFirebaseToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                callback(null)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, "FCM Registration Token: $token")
            callback(token)
        }
    }

    /**
     * Subscribe to a topic for receiving notifications
     */
    @SuppressLint("LongLogTag")
    fun subscribeToTopic(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully subscribed to topic: $topic")
                } else {
                    Log.e(TAG, "Failed to subscribe to topic: $topic", task.exception)
                }
            }
    }

    /**
     * Unsubscribe from a topic
     */
    @SuppressLint("LongLogTag")
    fun unsubscribeFromTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully unsubscribed from topic: $topic")
                } else {
                    Log.e(TAG, "Failed to unsubscribe from topic: $topic", task.exception)
                }
            }
    }

    /**
     * Create notification channel for Android O and above
     */
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Check if notifications are enabled
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun areNotificationsEnabled(): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    /**
     * Request notification permission (for Android 13+)
     */
    @SuppressLint("LongLogTag")
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // This should be called from an Activity or Fragment
            // The actual permission request should be handled in the UI layer
            Log.d(TAG, "Notification permission required for Android 13+")
        }
    }
}





