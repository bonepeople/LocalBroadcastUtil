package com.bonepeople.android.localbroadcastutil.simple;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastUtil.init(this);
        LocalBroadcastUtil.setDebugEnable(true);
        LocalBroadcastUtil.setLogger(new LocalBroadcastUtil.Logger() {
            @Override
            public void log(@NonNull String content) {
                Log.d("LocalBroadcastSimple", content);
            }
        });
    }
}
