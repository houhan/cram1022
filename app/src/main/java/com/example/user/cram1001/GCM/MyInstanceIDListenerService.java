package com.example.user.cram1001.GCM;

/**
 * Created by user on 2016/10/8.
 */

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        AsyncTask<Void, Void, Void> getTokenTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String token = GCMUtils.getGCMToken(MyInstanceIDListenerService.this);
                if (!TextUtils.isEmpty(token)) {
                    GCMUtils.saveToken(MyInstanceIDListenerService.this, token);
                }
                return null;
            }
        };
        getTokenTask.execute();
    }
    // [END refresh_token]
}