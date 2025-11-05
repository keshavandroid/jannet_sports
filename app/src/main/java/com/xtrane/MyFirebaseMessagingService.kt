package com.xtrane

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.xtrane.ui.coachApp.CoachMainActivity
import com.xtrane.ui.parentsApp.ParentsMainActivity
import com.xtrane.utils.FirebaseNotificationHelper
import com.xtrane.utils.SharedPrefUserData

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null

    var context: Context? = null
    var type: String? = null
    override fun onCreate() {
        super.onCreate()
        context = this
        // Create notification channel
        val helper = FirebaseNotificationHelper(this)
        helper.createNotificationChannel()
    }

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + "onMessageReceived")
        Log.d(TAG, "From: " + remoteMessage.from)
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "Message notification payload: " + remoteMessage.notification)

        context = this

        // Play notification sound
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            Log.e(TAG, "Error playing notification sound", e)
        }

        // Initialize NotificationManager here
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels()
        }

        // Handle both data and notification payloads
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            sendNotification(remoteMessage.data,remoteMessage.notification)
        }
//        else if (remoteMessage.notification != null) {
//            Log.d( TAG, "Message Notification Body: " + remoteMessage.notification!!.body
//            )
//            sendNotification(null, remoteMessage.notification)
//        }
    }

    @SuppressLint("LongLogTag")
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")


        // Send the new token to your server
        sendRegistrationToServer(token)
    }

    /**
     * Send token to server for storing and sending notifications
     */
    @SuppressLint("LongLogTag")
    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement server call to save the token
        Log.d(
            TAG,
            "Token should be sent to server: $token"
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val adminChannelName: CharSequence = ANDROID_CHANNEL_NAME
        val adminChannel = NotificationChannel(
            ANDROID_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        adminChannel.setSound(defaultSoundUri, attributes)
        adminChannel.description = FirebaseNotificationHelper.CHANNEL_DESCRIPTION
        if (notificationManager != null) {
            notificationManager!!.createNotificationChannel(adminChannel)
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param dataPayload FCM data payload received.
     * @param notificationPayload FCM notification payload received.
     */
//    @SuppressLint("LongLogTag")
//    private fun sendNotification(
//        dataPayload: Map<String, String?>?,
//        notificationPayload: RemoteMessage.Notification?
//    ) {
//        try {
//            Log.d(
//                TAG,
//                "Sending notification with data: $notificationPayload"
//            )
//
//            val icon = BitmapFactory.decodeResource(context!!.resources, R.mipmap.ic_launcher)
//            notificationBuilder = NotificationCompat.Builder(context!!, ANDROID_CHANNEL_ID)
//
//            var title: String? = "Xtrane"
//            var message: String? = "You have a new notification"
//
//            // Extract title and message from notification payload or data payload
//            if (notificationPayload != null) {
//                title = if (notificationPayload.title != null) notificationPayload.title else title
//                message =
//                    if (notificationPayload.body != null) notificationPayload.body else message
//            }
//            else {
//                title = "Xtrane"
//                message = "You have a new notification"
//            }
//
//            Log.d(
//                TAG,
//                "Notification - Title: $title, Message: $message"
//            )
//            // Create intent for notification click
//            val userType: String =
//                SharedPrefUserData(applicationContext).getSavedData().usertype.toString()
//            Log.d(TAG, "Notification - userType: $userType")
//            // Create intent for notification click
//            val intent: Intent
//
//            if (userType.equals("coach")) {
//                intent = Intent(this, CoachMainActivity::class.java)
//                //intent.setAction("FCM_MESSAGE")
//
//
//            } else if (userType.equals("parent")) {
//                intent = Intent(this, ParentsMainActivity::class.java)
//                //intent.setAction("FCM_MESSAGE")
//
//            } else {
//                intent = Intent(this, ParentsMainActivity::class.java)
//              //  intent.setAction("FCM_MESSAGE")
//
//            }
//
////            intent.putExtra("from", "notification")
////            intent.putExtra("type", "EventReminder")
//           // intent.putExtra("message", message+"")
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//            val pendingIntent = PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            // To ensure the data from the notification is delivered to the Activity even when the app is closed,
//            // save the intent extras to SharedPreferences here. Then, in the Activity's onCreate, retrieve and use them.
//
//            // Save notification data to SharedPreferences
//            val sharedPref = applicationContext.getSharedPreferences("NotificationData", Context.MODE_PRIVATE)
//
//            sharedPref.edit().apply {
//                putString("from", "notification")
//                putString("type", " EventOwnerReminder")
//                putString("message", message ?: "")
//                apply()
//            }
//            // To retrieve this data in your Activity, use the following code:
//
//
//
//
//            // Save notification data to SharedPreferences
//
//            val notificationId = System.currentTimeMillis().toInt()
//
//            notificationBuilder!!
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(icon)
//                .setContentTitle("Xtrane")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//
//            // Ensure notificationManager is not null before using it
//            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//            notificationManager.notify(notificationId, notificationBuilder!!.build())
//            Log.d(
//                TAG,
//                "Notification sent with ID: $notificationId"
//            )
//        } catch (e: Exception) {
//            Log.e(TAG, "Error creating notification", e)
//        }
//    }
    @SuppressLint("LongLogTag")
    private fun sendNotification(
        dataPayload: Map<String, String?>?,
        notification: RemoteMessage.Notification?
    ) {
        try {
            Log.d(
                TAG,
                "Sending notification with data: $notification"
            )

            val icon = BitmapFactory.decodeResource(context!!.resources, R.mipmap.ic_launcher)
            notificationBuilder = NotificationCompat.Builder(context!!, ANDROID_CHANNEL_ID)

            var title: String? = "Xtrane"
            var message: String? = "You have a new notification"
            type= "notification"

            // Extract title and message from notification payload or data payload
            if (dataPayload != null) {
                title = if (notification!!.title!= null) notification.title else title
                message = if (notification.body!= null) notification.body else message
                type = if (dataPayload.get("type")!= null) dataPayload.get("type") else type
            }
            else {
                title = "Xtrane"
                message = "You have a new notification"
                type = "notification"
            }

            Log.d(
                TAG,
                "Notification - Title: $title, Message: $message"
            )
            // Create intent for notification click
            val userType: String = SharedPrefUserData(applicationContext).getSavedData().usertype.toString()
            Log.d(TAG, "Notification - userType: $userType")
            // Create intent for notification click
            val intent: Intent

            if (userType.equals("coach")) {
                intent = Intent(this, CoachMainActivity::class.java)
                //intent.setAction("FCM_MESSAGE")


            } else if (userType.equals("parent")) {
                intent = Intent(this, ParentsMainActivity::class.java)
                //intent.setAction("FCM_MESSAGE")

            } else {
                intent = Intent(this, ParentsMainActivity::class.java)
                //  intent.setAction("FCM_MESSAGE")

            }

            intent.putExtra("from", "notification")
            intent.putExtra("title", title)
            intent.putExtra("type", type)
            intent.putExtra("message", message+"")
            intent.putExtra("eventId", dataPayload!!.get("eventId")+"==")

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationId = System.currentTimeMillis().toInt()

            notificationBuilder!!
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle("Xtrane")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            // Ensure notificationManager is not null before using it
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(notificationId, notificationBuilder!!.build())
            Log.d(
                TAG,
                "Notification sent with ID: $notificationId"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error creating notification", e)
        }
    }
    companion object {
        private const val TAG = "FirebaseMessagingService"
        const val ANDROID_CHANNEL_ID: String = FirebaseNotificationHelper.CHANNEL_ID
        const val ANDROID_CHANNEL_NAME: String = FirebaseNotificationHelper.CHANNEL_NAME
        private fun buildContentIntentLike(
            context: Context,
            activityClass: Class<out Activity?>,
            cid: Int,
            sid: Int
        ): PendingIntent? {
            val intent = Intent(context, activityClass)
            intent.putExtra("cid", cid)
            intent.putExtra("sid", sid)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)


            var pendingIntent: PendingIntent? = null


            pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            return pendingIntent
        }
    }
}

// LocalBroadcastManager.getInstance(this).sendBroadcast(intent)


//            intent.putExtra("type","EventOwnerReminder");
//            intent.putExtra("type","CoachTurn");