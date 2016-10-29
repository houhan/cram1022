package com.example.user.cram1001.Fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by user on 2016/10/26.
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {

        @Override
        public void onTokenRefresh() {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.d("FCM", "Token:"+token);

    }
}
