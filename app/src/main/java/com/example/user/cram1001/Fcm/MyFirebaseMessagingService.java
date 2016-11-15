package com.example.user.cram1001.Fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.user.cram1001.CheckActivity;
import com.example.user.cram1001.FcmActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by user on 2016/10/26.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "MyFirebaseMsgService";

    public MyFirebaseMessagingService() {
        super();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        String message = remoteMessage.getNotification().getBody().toString();
        String data_from = remoteMessage.getData().get("send_from");
        String data_type = remoteMessage.getData().get("send_type");
        String first_point = remoteMessage.getData().get("first_point");
        String second_point = remoteMessage.getData().get("second_point");
        Log.d(TAG,"FCMmessage:"+message);

        sendNotification(message,data_from,data_type,first_point,second_point);

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String data_from, String data_type,
                                  String first_point, String second_point) {
        /**如果要帶其他值在裡面,用JSONObject裝然後再拆解(下面是當時post的body)
         *{
         "to": "cl3nf73rKEU:APA91bF46TrTaoEdv9EaH8GcHe3-2ufrN57IT1uMX_eT5AlDIAhESu3-h5_fNbMgHXChpr7ZvHmTjhxybSr1bwAIERL7Dt3iR2db0pX0zQb2-8DiIlc2z34wYmxZdq6OWA-ucK3CfuTw",
         "notification": {
         "sound": "default",
         "title": "HangOutFCM",
         "body": {
         "message":"FCM_test",
         "from": "Samuel"
         }
         },
         "priority" : "high"
         */

        Log.d(TAG,"from="+data_from);
        Log.d(TAG,"type="+data_type);
        Log.d(TAG,"point1="+first_point+",point2="+second_point);
//        String message = null,from = null;
//        try {
//            JSONObject jsonObject = new JSONObject(messageBody);
//            message = jsonObject.optString("message");
//            from = jsonObject.optString("from");
//            Intent intent = new Intent("GCM_message");
//            intent.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(this, CheckActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder;
        if("message".equals(data_type)) {
            notificationBuilder = new NotificationCompat.Builder(this)
                   // .setSmallIcon(R.mipmap.android_logo)
                    .setContentTitle("Cram(" + data_from + ")")
                    .setContentText(messageBody)
                    .setAutoCancel(true);
//                    .setContentIntent(pendingIntent);
        }else if("team_invite".equals(data_type)){

            String point1=null,lat1=null,lng1=null,point2=null,lat2=null,lng2=null;

            try{
                JSONObject jsonObject1 = new JSONObject(first_point);
                JSONObject jsonObject2 = new JSONObject(second_point);
                point1 = jsonObject1.optString("point_name");
                lat1 = jsonObject1.optString("point_lat");
                lng1 = jsonObject1.optString("point_lng");
                point2 = jsonObject2.optString("point_name");
                lat2 = jsonObject2.optString("point_lat");
                lng2 = jsonObject2.optString("point_lng");
            }catch (JSONException e){
                e.printStackTrace();
            }

            notificationBuilder = new NotificationCompat.Builder(this)
                    //  .setSmallIcon(R.mipmap.android_logo)
                    .setContentTitle("HangOut(跟車邀請)")
                    .setContentText(messageBody)
                    .setAutoCancel(true);
//                    .setContentIntent(pendingIntent);

            Intent i = new Intent("Team_Invite");
            i.putExtra("teamName", data_from);
            i.putExtra("point1", point1);
            i.putExtra("lat1", lat1);
            i.putExtra("lng1", lng1);
            i.putExtra("point2", point2);
            i.putExtra("lat2", lat2);
            i.putExtra("lng2", lng2);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
        }else{
            notificationBuilder = new NotificationCompat.Builder(this)
                 //   .setSmallIcon(R.mipmap.android_logo)
                    .setContentTitle("HangOut")
                    .setContentText("無法辨識的訊息")
                    .setAutoCancel(true);
//                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
