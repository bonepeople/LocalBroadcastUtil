package com.bonepeople.android.localbroadcastutil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * 本地广播工具类
 *
 * 使本地广播的使用更方便
 *
 * @author bonepeople
 */
object LocalBroadcastUtil {
    var logger: Logger = SimpleLogger
    var DEBUG = false
    private lateinit var applicationContext: Context
    private val broadcastManager: LocalBroadcastManager by lazy { LocalBroadcastManager.getInstance(applicationContext) }

    /**
     * 初始化本地广播工具类，建议放到Application的onCreate函数中执行，仅保存Application，不会执行耗时操作
     */
    fun saveApplication(context: Context) {
        applicationContext = context.applicationContext
    }

    /**
     * 注册广播接收器
     *
     * 该方法重复调用仅会使用最新的参数注册一次
     *
     * @param receiver 非空的广播接收器
     * @param actions  广播的筛选字段，该字段会被加入到action中，需要包含至少一个广播筛选字段
     */
    fun registerReceiver(receiver: BroadcastReceiver, vararg actions: String) {
        registerReceiver(null, receiver, *actions)
    }

    /**
     * 注册广播接收器
     *
     *
     * 广播接收器将绑定至LifecycleOwner，在onDestroy的时候自动注销，Activity和Fragment均已实现LifecycleOwner接口
     *
     * 该方法重复调用仅会使用最新的参数注册一次
     *
     * @param lifecycleOwner 被绑定的Activity、Fragment
     * @param receiver       非空的广播接收器
     * @param actions        广播的筛选字段，该字段会被加入到action中，需要包含至少一个广播筛选字段
     */
    fun registerReceiver(lifecycleOwner: LifecycleOwner?, receiver: BroadcastReceiver, vararg actions: String) {
        require(actions.isNotEmpty()) { "注册广播需要包含至少一个广播筛选字段" }
        val filter = IntentFilter().apply {
            actions.forEach {
                addAction(it)
            }
        }
        registerReceiver(lifecycleOwner, receiver, filter)
    }

    /**
     * 注册广播接收器
     *
     * 该方法重复调用仅会使用最新的参数注册一次
     *
     * @param receiver 非空的广播接收器
     * @param filter   广播筛选条件
     */
    fun registerReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
        registerReceiver(null, receiver, filter)
    }

    /**
     * 注册广播接收器
     *
     *
     * 广播接收器将绑定至LifecycleOwner，在onDestroy的时候自动注销，Activity和Fragment均已实现LifecycleOwner接口
     *
     * 该方法重复调用仅会使用最新的参数注册一次
     *
     * @param lifecycleOwner 被绑定的Activity、Fragment
     * @param receiver       非空的广播接收器
     * @param filter         广播筛选条件
     */
    fun registerReceiver(lifecycleOwner: LifecycleOwner?, receiver: BroadcastReceiver, filter: IntentFilter) {
        lifecycleOwner?.let {
            bindLifecycle(lifecycleOwner, receiver)
        }
        //防止重复注册，在注册之前先注销原有接收器
        broadcastManager.unregisterReceiver(receiver)
        broadcastManager.registerReceiver(receiver, filter)
        if (DEBUG) {
            val count = filter.countActions()
            val stringBuilder = StringBuilder("注册广播接收器 $receiver 响应 [")
            var i = 0
            while (i < count) {
                stringBuilder.append(filter.getAction(i))
                if (++i == count) stringBuilder.append("]") else stringBuilder.append(",")
            }
            logger.log(stringBuilder.toString())
        }
    }

    /**
     * 注销广播接收器
     *
     * 未注册过的接收器进行注销操作不会导致异常
     *
     * @param receiver 非空的广播接收器
     */
    fun unregisterReceiver(receiver: BroadcastReceiver) {
        broadcastManager.unregisterReceiver(receiver)
        if (DEBUG) {
            logger.log("$receiver 的监听已注销")
        }
    }

    /**
     * 绑定广播接收器至界面的生命周期
     *
     * Activity和Fragment均已实现LifecycleOwner接口，可以直接绑定它们的生命周期
     *
     * @param lifecycleOwner 被绑定的Activity、Fragment
     * @param receiver       需要在Activity、Fragment销毁时注销的广播接收器
     */
    fun bindLifecycle(lifecycleOwner: LifecycleOwner, receiver: BroadcastReceiver) {
        lifecycleOwner.lifecycle.addObserver(OnDestroyListener(receiver))
        if (DEBUG) {
            logger.log("将 $receiver 绑定至 $lifecycleOwner 的生命周期中")
        }
    }

    /**
     * 发送本地广播
     *
     * 不需要附加参数的广播可以使用此方式快速发送
     *
     * @param action 广播的筛选字段
     */
    fun sendBroadcast(action: String?) {
        sendBroadcast(Intent(action))
    }

    /**
     * 发送本地广播
     *
     * @param intent 需要发送的广播内容，intent的action字段必须设置为广播的筛选字段
     */
    fun sendBroadcast(intent: Intent) {
        broadcastManager.sendBroadcast(intent)
        if (DEBUG) {
            logger.log("发送action为 " + intent.action + " 的广播")
        }
    }

    /**
     * 日志输出接口
     *
     * 可以实现此接口进行日志的输出。
     */
    interface Logger {
        fun log(content: String)
    }
}