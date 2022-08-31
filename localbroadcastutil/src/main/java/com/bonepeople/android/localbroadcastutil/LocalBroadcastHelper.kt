package com.bonepeople.android.localbroadcastutil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleOwner

/**
 * LocalBroadcastUtil广播注册助手
 * + 使用建造者模式方便用户更简单的使用[LocalBroadcastUtil]注册广播
 * @author bonepeople
 */
class LocalBroadcastHelper {
    private var lifecycleOwner: LifecycleOwner? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private var intentFilter: IntentFilter? = null

    /**
     * 设置生命周期监听
     * + 注册广播时会将生命周期绑定至提供的宿主上
     */
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner?) = apply {
        this.lifecycleOwner = lifecycleOwner
    }

    /**
     * 设置接收广播后的回调函数
     * + 接收到的[Intent]会以参数形式提供
     * + 调用该方法会生成一个新的[BroadcastReceiver]用于注册广播，在注册前请先妥善处理之前已经注册的广播接收器
     * + 会覆盖现有的广播接收器
     */
    fun onReceive(receiveAction: (intent: Intent) -> Unit) = apply {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                receiveAction(intent)
            }
        }
    }

    /**
     * 设置广播接收器
     * + 会覆盖现有的广播接收器
     */
    fun setReceiver(receiver: BroadcastReceiver) = apply {
        broadcastReceiver = receiver
    }

    /**
     * 添加广播筛选字段
     * @param actions 广播的筛选字段，该字段会被加入到action中，需要包含至少一个广播筛选字段
     */
    fun addAction(vararg actions: String) = apply {
        if (intentFilter == null) {
            require(actions.isNotEmpty()) { "注册广播需要包含至少一个广播筛选字段" }
            intentFilter = IntentFilter()
        }
        actions.forEach {
            intentFilter?.addAction(it)
        }
    }

    /**
     * 设置广播筛选条件
     * + 会覆盖现有的广播筛选条件
     */
    fun setFilter(filter: IntentFilter) = apply {
        intentFilter = filter
    }

    /**
     * 将已配置好的信息注册至[LocalBroadcastUtil]
     * + 注册之前请确保已经设置了广播接收器和广播筛选条件
     * @return 将用于注册的[BroadcastReceiver]返回，后续可以用于注销广播或重新注册广播
     */
    fun register(): BroadcastReceiver {
        require(broadcastReceiver != null) { "需要通过[onReceive]或[setReceiver]设置广播接收器" }
        require(intentFilter != null) { "需要通过[addAction]或[setFilter]设置广播筛选条件" }
        LocalBroadcastUtil.registerReceiver(lifecycleOwner, broadcastReceiver!!, intentFilter!!)
        return broadcastReceiver!!
    }

    /**
     * 注册广播的快捷方法
     * @return 将用于注册的[BroadcastReceiver]返回，后续可以用于注销广播或重新注册广播
     */
    fun register(lifecycleOwner: LifecycleOwner?, vararg actions: String, receiveAction: (Intent) -> Unit): BroadcastReceiver {
        setLifecycleOwner(lifecycleOwner)
        onReceive(receiveAction)
        addAction(*actions)
        return register()
    }
}