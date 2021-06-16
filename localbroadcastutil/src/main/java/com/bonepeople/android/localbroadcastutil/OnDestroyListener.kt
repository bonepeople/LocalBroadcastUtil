package com.bonepeople.android.localbroadcastutil

import android.content.BroadcastReceiver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class OnDestroyListener(private val receiver: BroadcastReceiver) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregister(lifecycleOwner: LifecycleOwner) {
        LocalBroadcastUtil.unregisterReceiver(receiver)
        lifecycleOwner.lifecycle.removeObserver(this)
        if (LocalBroadcastUtil.DEBUG) {
            LocalBroadcastUtil.logger.log("已自动注销 $receiver")
        }
    }

    override fun hashCode(): Int {
        return receiver.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is OnDestroyListener) {
            other.receiver == receiver
        } else false
    }
}