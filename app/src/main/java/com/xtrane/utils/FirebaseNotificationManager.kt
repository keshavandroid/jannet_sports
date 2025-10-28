package com.xtrane.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseNotificationManager(private val context: Context) {

    companion object {
        private const val TAG = "FirebaseNotificationManager"
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }

    /**
     * Initialize Firebase notifications
     */
    @SuppressLint("LongLogTag")
    fun initializeFirebaseNotifications() {
        Log.d(TAG, "Initializing Firebase notifications")
        
        // Create notification channel
        val helper = FirebaseNotificationHelper(context)
        helper.createNotificationChannel()
        
        // Get FCM token
        getFirebaseToken { token ->
            if (token != null) {
                Log.d(TAG, "FCM Token obtained: $token")
                // TODO: Send token to your server
                sendTokenToServer(token)
            } else {
                Log.e(TAG, "Failed to get FCM token")
            }
        }
        
        // Subscribe to general topics
        subscribeToGeneralTopics()
    }

    /**
     * Get Firebase Cloud Messaging token
     */
    @SuppressLint("LongLogTag")
    private fun getFirebaseToken(callback: (String?) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                callback(null)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d(TAG, "FCM Registration Token: $token")
            callback(token)
        }
    }

    /**
     * Send token to server
     */
    @SuppressLint("LongLogTag")
    private fun sendTokenToServer(token: String) {
        // TODO: Implement API call to send token to your server
        // This should be called with user authentication details
        Log.d(TAG, "Token should be sent to server: $token")
        
        // Example implementation:
        // val apiService = RetrofitClient.getApiService()
        // apiService.updateFCMToken(userId, token).enqueue(...)
    }

    /**
     * Subscribe to general notification topics
     */
    @SuppressLint("LongLogTag")
    private fun subscribeToGeneralTopics() {
        val topics = listOf(
            "general_notifications",
            "event_updates",
            "system_announcements"
        )
        
        topics.forEach { topic ->
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully subscribed to topic: $topic")
                    } else {
                        Log.e(TAG, "Failed to subscribe to topic: $topic", task.exception)
                    }
                }
        }
    }

    /**
     * Subscribe user to specific topics based on their role and preferences
     */
    @SuppressLint("LongLogTag")
    fun subscribeToUserTopics(userId: String, userType: String, sports: List<String>) {
        val topics = mutableListOf<String>()
        
        // Add user-specific topic
        topics.add("user_$userId")
        
        // Add role-specific topics
        when (userType.lowercase()) {
            "coach" -> topics.add("coach_notifications")
            "parent" -> topics.add("parent_notifications")
            "player" -> topics.add("player_notifications")
        }
        
        // Add sport-specific topics
        sports.forEach { sport ->
            topics.add("sport_${sport.lowercase()}")
        }
        
        // Subscribe to all topics
        topics.forEach { topic ->
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully subscribed to topic: $topic")
                    } else {
                        Log.e(TAG, "Failed to subscribe to topic: $topic", task.exception)
                    }
                }
        }
    }

    /**
     * Unsubscribe from topics
     */
    @SuppressLint("LongLogTag")
    fun unsubscribeFromTopics(topics: List<String>) {
        topics.forEach { topic ->
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully unsubscribed from topic: $topic")
                    } else {
                        Log.e(TAG, "Failed to unsubscribe from topic: $topic", task.exception)
                    }
                }
        }
    }

    /**
     * Check if notification permission is granted
     */
    fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permission not required for older versions
        }
    }

    /**
     * Request notification permission (should be called from Activity)
     */
    fun requestNotificationPermission(activity: android.app.Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isNotificationPermissionGranted()) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
}
