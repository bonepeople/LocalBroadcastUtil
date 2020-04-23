package com.bonepeople.android.localbroadcastutil;

import android.content.BroadcastReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

class OnDestroyListener implements LifecycleObserver {
    private BroadcastReceiver receiver;

    OnDestroyListener(@NonNull BroadcastReceiver receiver) {
        this.receiver = receiver;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(@NonNull LifecycleOwner lifecycleOwner) {
        LocalBroadcastUtil.unregisterReceiver(receiver);
        lifecycleOwner.getLifecycle().removeObserver(this);
        if (LocalBroadcastUtil.debugEnable) {
            Log.d(LocalBroadcastUtil.TAG, "已自动注销 " + receiver.toString());
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
