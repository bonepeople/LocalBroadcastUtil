package com.bonepeople.android.localbroadcastutil.simple;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastUtil.INSTANCE.saveApplication(this);
        LocalBroadcastUtil.INSTANCE.setDEBUG(true);
        LocalBroadcastUtil.INSTANCE.setLogger(new LocalBroadcastUtil.Logger() {
            @Override
            public void log(@NonNull String content) {
                Log.d("LocalBroadcastSimple", content);
            }
        });
    }
}
