package com.bonepeople.android.localbroadcastutil.simple;

import android.app.Application;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastUtil.init(this);
        LocalBroadcastUtil.setDebugEnable(true);
    }
}
