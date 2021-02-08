package com.bonepeople.android.localbroadcastutil;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * 简单的日志输出类，使用系统的Log类进行输出。
 */
public class SimpleLogger implements LocalBroadcastUtil.Logger {
    private static final String TAG = "LocalBroadcastUtil";

    @Override
    public void log(@NonNull String content) {
        Log.d(TAG, content);
    }
}
