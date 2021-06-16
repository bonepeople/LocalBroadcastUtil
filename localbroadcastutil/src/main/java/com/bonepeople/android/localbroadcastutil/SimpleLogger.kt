package com.bonepeople.android.localbroadcastutil

import android.util.Log

/**
 * 简单的日志输出类，使用系统的Log类进行输出。
 */
object SimpleLogger : LocalBroadcastUtil.Logger {
    private const val TAG = "LocalBroadcastUtil"
    override fun log(content: String) {
        Log.d(TAG, content)
    }
}