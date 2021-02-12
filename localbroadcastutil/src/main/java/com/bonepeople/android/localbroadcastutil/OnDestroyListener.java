package com.bonepeople.android.localbroadcastutil;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class OnDestroyListener implements LifecycleObserver {
    private final BroadcastReceiver receiver;

    OnDestroyListener(@NonNull BroadcastReceiver receiver) {
        this.receiver = receiver;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void unregister(@NonNull LifecycleOwner lifecycleOwner) {
        LocalBroadcastUtil.unregisterReceiver(receiver);
        lifecycleOwner.getLifecycle().removeObserver(this);
        if (LocalBroadcastUtil.debugEnable) {
            LocalBroadcastUtil.logger.log("已自动注销 " + receiver.toString());
        }
    }

    @Override
    public int hashCode() {
        return receiver.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof OnDestroyListener) {
            return ((OnDestroyListener) obj).receiver.equals(receiver);
        }
        return false;
    }
}
