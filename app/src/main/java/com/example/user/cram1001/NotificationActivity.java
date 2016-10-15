package com.example.user.cram1001;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotificationActivity extends AppCompatActivity {


    private Button btnSend;
    private EditText etMessage;
    private int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);
        btnSend = (Button) findViewById(R.id. button1);
        etMessage = (EditText) findViewById(R.id. editText1);

        btnSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendNotification( etMessage.getText().toString());
                etMessage.setText( "");

            }

        });

    }
/*
    public void sendNotificationOld(String message) {

        // -- 舊的寫法

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE );
        Notification notification = new Notification(R.drawable. logo2,
                message, System. currentTimeMillis());
        PendingIntent contentIndent = PendingIntent.getActivity(
                GcmActivity. this, 0, new Intent(GcmActivity.this,
                        GcmActivity. class), PendingIntent.FLAG_UPDATE_CURRENT );

        notification. setLatestEventInfo(GcmActivity. this, "Notification " + i,
                message, contentIndent) ; // 加i是為了顯示多條Notification
        notificationManager.notify( i, notification);
        i++;

        // --

    }
*/
    @SuppressWarnings("deprecation")
    public void sendNotification(String message) {

        try {

            // -- 新的寫法
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE );
            Notification.Builder builder = new Notification.Builder(NotificationActivity.this );
            PendingIntent contentIndent = PendingIntent.getActivity(
                    NotificationActivity. this, 0, new Intent(NotificationActivity.this,
                            NotificationActivity. class),
                    PendingIntent. FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIndent)
                    .setSmallIcon(R.drawable. logo2)
                    // 設置狀態列裡面的圖示（小圖示）　　　　　　　　　　　
                    //.setLargeIcon(BitmapFactory. decodeResource(this.getResources(),R.drawable. logo2)) // 下拉下拉清單裡面的圖示（大圖示）
                    .setTicker(message) // 設置狀態列的顯示的資訊
                    .setWhen(System. currentTimeMillis())// 設置時間發生時間
                    .setAutoCancel( false) // 設置可以清除
                    .setContentTitle( "智慧安心班 " + i) // 設置下拉清單裡的標題
                    .setContentText(message); // 設置上下文內容

            Notification notification = builder.getNotification();

            // notification.defaults |= Notification.DEFAULT_SOUND;
            notification. sound = Uri
                    . parse("file:///sdcard/Notifications/hangout_ringtone.m4a");
            notification. sound = Uri. withAppendedPath(
                    MediaStore.Audio.Media. INTERNAL_CONTENT_URI, "6");
          //  notification. sound = Uri.parse("android.resource://"
                   // + getPackageName() + "/" + R.raw.koko);
            // 後面的設定會蓋掉前面的

            // 振動
            notification. defaults |= Notification.DEFAULT_VIBRATE ; // 某些手機不支援 請加
            // try catch

            // 閃光燈
            notification. defaults |= Notification.DEFAULT_LIGHTS; // 測試沒反應


            // 加i是為了顯示多條Notification
            notificationManager.notify(1, notification);
            i++;
            // --
        } catch (Exception e) {

        }
    }
}