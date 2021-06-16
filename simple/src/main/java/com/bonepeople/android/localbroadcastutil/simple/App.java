package com.bonepeople.android.localbroadcastutil.simple;

import android.app.Application;
import android.util.Log;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastUtil.INSTANCE.setDebug(true);
        LocalBroadcastUtil.INSTANCE.setLogger(content -> Log.d("LocalBroadcastSimple", content));
    }
}
