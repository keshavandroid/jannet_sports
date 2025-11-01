package com.xtrane;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.xtrane.utils.MainApplication;
import com.xtrane.utils.FirebaseNotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingService";
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    public static final String ANDROID_CHANNEL_ID = FirebaseNotificationHelper.CHANNEL_ID;
    public static final String ANDROID_CHANNEL_NAME = FirebaseNotificationHelper.CHANNEL_NAME;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // Create notification channel
        FirebaseNotificationHelper helper = new FirebaseNotificationHelper(this);
        helper.createNotificationChannel();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + "onMessageReceived");

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "Message notification payload: " + remoteMessage.getNotification());

        context = this;

        // Play notification sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            Log.e(TAG, "Error playing notification sound", e);
        }

        // Initialize NotificationManager here
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }

        // Handle both data and notification payloads
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getData(), remoteMessage.getNotification());
        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(null, remoteMessage.getNotification());
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        
        // Send the new token to your server
        sendRegistrationToServer(token);
    }

    /**
     * Send token to server for storing and sending notifications
     */
    @SuppressLint("LongLogTag")
    private void sendRegistrationToServer(String token) {
        // TODO: Implement server call to save the token
        Log.d(TAG, "Token should be sent to server: " + token);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        CharSequence adminChannelName = ANDROID_CHANNEL_NAME;
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ANDROID_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        adminChannel.setSound(defaultSoundUri, attributes);
        adminChannel.setDescription(FirebaseNotificationHelper.CHANNEL_DESCRIPTION);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param dataPayload FCM data payload received.
     * @param notificationPayload FCM notification payload received.
     */
    @SuppressLint("LongLogTag")
    private void sendNotification(Map<String, String> dataPayload, RemoteMessage.Notification notificationPayload) {

        try {
            Log.d(TAG, "Sending notification with data: " + dataPayload);
            
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            notificationBuilder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID);

            String title = "Jannet";
            String message = "You have a new notification";

            // Extract title and message from notification payload or data payload
            if (notificationPayload != null) {
                title = notificationPayload.getTitle() != null ? notificationPayload.getTitle() : title;
                message = notificationPayload.getBody() != null ? notificationPayload.getBody() : message;
            }
            else if (dataPayload != null) {
                // Try to extract from data payload
                title = dataPayload.get("title") != null ? dataPayload.get("title") : title;
                message = dataPayload.get("body") != null ? dataPayload.get("body") : message;
            }

            Log.d(TAG, "Notification - Title: " + title + ", Message: " + message);

            // Create intent for notification click

            Intent intent = new Intent(this, com.xtrane.ui.commonApp.NotificationsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            int notificationId = (int) System.currentTimeMillis();
            notificationBuilder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            // Ensure notificationManager is not null before using it
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(notificationId, notificationBuilder.build());
                Log.d(TAG, "Notification sent with ID: " + notificationId);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error creating notification", e);
        }
    }

    private static PendingIntent buildContentIntentLike(Context context, Class<? extends Activity> activityClass,Integer cid,Integer sid) {
        Intent intent;
        intent = new Intent(context, activityClass);
        intent.putExtra("cid",cid);
        intent.putExtra("sid",sid);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent pendingIntent = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return pendingIntent;
    }

}
