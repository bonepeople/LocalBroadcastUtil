package com.bonepeople.android.localbroadcastutil;

import android.arch.lifecycle.LifecycleOwner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Objects;

/**
 * 本地广播工具类
 * <p>使本地广播的使用更方便</p>
 *
 * @author bonepeople
 */
public class LocalBroadcastUtil {
    private static LocalBroadcastManager broadcastManager;

    private LocalBroadcastUtil() {
    }

    /**
     * 初始化本地广播工具类，建议放到Application的onCreate函数中执行
     */
    public static void init(@NonNull Context context) {
        Objects.requireNonNull(context);
        broadcastManager = LocalBroadcastManager.getInstance(context);
    }

    /**
     * 注册广播接收器
     * <p>该方法重复调用仅会使用最新的参数注册一次</p>
     *
     * @param receiver 非空的广播接收器
     * @param actions  广播的筛选字段，该字段会被加入到action中，需要包含至少一个广播筛选字段
     */
    public static void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull String... actions) {
        registerReceiver(null, receiver, actions);
    }

    /**
     * 注册广播接收器
     * <p>
     * 广播接收器将绑定至LifecycleOwner，在onDestroy的时候自动注销，Activity和Fragment均已实现LifecycleOwner接口
     * <p>该方法重复调用仅会使用最新的参数注册一次</p>
     *
     * @param lifecycleOwner 被绑定的Activity、Fragment
     * @param receiver       非空的广播接收器
     * @param actions        广播的筛选字段，该字段会被加入到action中，需要包含至少一个广播筛选字段
     */
    public static void registerReceiver(@Nullable LifecycleOwner lifecycleOwner, @NonNull BroadcastReceiver receiver, @NonNull String... actions) {
        if (actions.length < 1)
            throw new IllegalArgumentException("注册广播需要包含至少一个广播筛选字段");
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        registerReceiver(lifecycleOwner, receiver, filter);
    }

    /**
     * 注册广播接收器
     * <p>该方法重复调用仅会使用最新的参数注册一次</p>
     *
     * @param receiver 非空的广播接收器
     * @param filter   广播筛选条件
     */
    public static void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        registerReceiver(null, receiver, filter);
    }

    /**
     * 注册广播接收器
     * <p>
     * 广播接收器将绑定至LifecycleOwner，在onDestroy的时候自动注销，Activity和Fragment均已实现LifecycleOwner接口
     * <p>该方法重复调用仅会使用最新的参数注册一次</p>
     *
     * @param lifecycleOwner 被绑定的Activity、Fragment
     * @param receiver       非空的广播接收器
     * @param filter         广播筛选条件
     */
    public static void registerReceiver(@Nullable LifecycleOwner lifecycleOwner, @NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        checkNotNull(receiver, filter);
        if (lifecycleOwner != null)
            bindLifecycle(lifecycleOwner, receiver);
        //防止重复注册，在注册之前先注销原有接收器
        broadcastManager.unregisterReceiver(receiver);
        broadcastManager.registerReceiver(receiver, filter);
    }

    /**
     * 注销广播接收器
     * <p>未注册过的接收器进行注销操作不会导致异常</p>
     *
     * @param receiver 非空的广播接收器
     */
    public static void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        checkNotNull(receiver);
        broadcastManager.unregisterReceiver(receiver);
    }

    /**
     * 绑定广播接收器至界面的生命周期
     * <p>Activity和Fragment均已实现LifecycleOwner接口，可以直接绑定它们的生命周期</p>
     *
     * @param lifecycleOwner 被绑定的Activity、Fragment
     * @param receiver       需要在Activity、Fragment销毁时注销的广播接收器
     */
    public static void bindLifecycle(@NonNull LifecycleOwner lifecycleOwner, @NonNull BroadcastReceiver receiver) {
        checkNotNull(lifecycleOwner, receiver);
        lifecycleOwner.getLifecycle().addObserver(new OnDestroyListener(receiver));
    }

    /**
     * 发送本地广播
     * <p>不需要附加参数的广播可以使用此方式快速发送</p>
     *
     * @param action 广播的筛选字段
     */
    public static void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }

    /**
     * 发送本地广播
     *
     * @param intent 需要发送的广播内容，intent的action字段必须设置为广播的筛选字段
     */
    public static void sendBroadcast(@NonNull Intent intent) {
        checkNotNull(intent);
        broadcastManager.sendBroadcast(intent);
    }

    private static void checkNotNull(Object... objects) {
        if (broadcastManager == null)
            throw new IllegalStateException("使用本地广播前必须先调用init(Context)方法进行初始化");
        for (Object object : objects) {
            Objects.requireNonNull(object);
        }
    }
}
