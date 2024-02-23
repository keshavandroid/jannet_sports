package com.e.jannet_stable_code;

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

import com.e.jannet_stable_code.utils.MainApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "LLLL_Firebase: ";
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    public static final String ANDROID_CHANNEL_ID = "com.e.jannet_stable_code";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    Context context;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "onMessageReceived Data" + remoteMessage.getData());
        Log.e(TAG, "onMessageReceived Notification" + remoteMessage.getNotification());


        context = this;

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        /*if (!MainApplication.onAppForegrounded) {
            Log.e("LLLLLL_APP: ", String.valueOf(MainApplication.onAppForegrounded));
            if (remoteMessage.getData().size() > 0) {
                sendNotification(remoteMessage.getData());
            }
        }*/

        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        CharSequence adminChannelName = ANDROID_CHANNEL_NAME;
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ANDROID_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        adminChannel.setSound(defaultSoundUri, attributes);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Map<String, String> messageBody) {

        try {
            Log.d("LLLL_Mess_Body: ", messageBody.toString() + "");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            notificationBuilder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID);

            JSONObject object = new JSONObject(messageBody);
            //   JSONObject object = new JSONObject(messageBody.toString());

            String message = object.optString("body");
            String title = object.optString("body");
            Log.d("LLLL_Mess_Body: ", message + " == " +title);

            //new added
            /*PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, FirstActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(contentIntent);*/




            int id_notifaciton = (int) System.currentTimeMillis();
            notificationBuilder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)//a resource for your custom small icon
                    .setContentTitle(title)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)//the "title" value you sent in your notification
                    .setContentText(message) //ditto
                    .setAutoCancel(true)  //dismisses the notification on click
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            //new
            /*JSONObject jsonObject = new JSONObject(object.optString("data"));
            Log.e("WOW_NOTI_PAYLOAD",jsonObject.toString());*/
            /*notificationBuilder.setContentIntent(buildContentIntentLike(context, FirstActivity.class,
                    object.getInt("category"),
                    object.getInt("subcategory")));*/


            notificationManager.notify(id_notifaciton /* ID of notification */, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
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
