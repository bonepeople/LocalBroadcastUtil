# LocalBroadcastUtil - 本地广播工具类

## 简介
`LocalBroadcastUntil`是一个本地广播的工具类，它可以使我们更方便的使用`LocalBroadcastManager`。

`LocalBroadcastManager`是安卓系统提供的本地广播管理类，使用这个类可以方便的发送本地广播传递数据且不受线程的影响(消息处理逻辑都在主线程中进行)，在不引入第三方库的情况下，`LocalBroadcastManager`是本地全局消息处理的最佳方式。
> 如果你需要在Activity之间或者Fragment之间发送事件消息，而又不想自己编写回调接口，`LocalBroadcastUntil`会是一个非常方便的工具。
> 例如用户登录成功时，不需要使用startActivityForResult，也不需要传递回调函数，只需要发送一个登录成功的本地广播，在需要刷新的页面中添加响应代码，就可以轻松搞定一个全局状态更新的效果。

## 集成方法
使用Gradle构建工具集成：
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.bonepeople:LocalBroadcastUtil:1.4.0'
}
```

## 使用示例
##### 注册广播接收器
注册过程中需要的`LifecycleOwner`可以为空，该参数主要是用于生命周期的绑定，确保`receiver`会在界面销毁的时候自动注销。
> `Activity`自身可作为`LifecycleOwner`参数使用，`Fragment`可以调用`getViewLifecycleOwner()`方法获取相应的`LifecycleOwner`
1. `LocalBroadcastUntil`中保留了一个通用的注册方法，可以使用该方法直接注册广播接收器。
```kotlin
LocalBroadcastUtil.registerReceiver(lifecycleOwner, broadcastReceiver, intentFilter)
```
2. 除了基本的注册方法，还提供了`LocalBroadcastHelper`类用来简化注册广播的流程。
`LocalBroadcastHelper`采用了建造者模式，用户通过调用设置方法配置广播接收器，最后调用`register`方法进行注册。
> 使用`setLifecycleOwner`、`setReceiver`和`setFilter`方法可以直接设置相应的属性。
> 使用`onReceive`方法可以传入一个回调方法，该方法被包装为一个`BroadcastReceiver`，在接收到广播的时候直接调用，简化了用户自定义`BroadcastReceiver`的流程。
> 使用`addAction`方法可以直接传入一个或多个筛选条件，将这些筛选条件自动放到`IntentFilter`中并用于注册广播接收器。
```kotlin
//常规注册方法
LocalBroadcastHelper()
    .setLifecycleOwner(viewLifecycleOwner)
    .addAction("BROADCAST_INCREASE", "BROADCAST_REDUCE")
    .onReceive {
        it.action
    }
    .register()

//快捷注册方法
LocalBroadcastHelper().register(viewLifecycleOwner, "BROADCAST_INCREASE", "BROADCAST_REDUCE") {
    it.action
}
```
##### 发送广播
如果是简单的消息广播，可直接调用`sendBroadcast(String)`发送广播，如果需要传递数据，可以自定义`Intent`并调用`sendBroadcast(Intent)`发送广播。
```kotlin
LocalBroadcastUtil.sendBroadcast("BROADCAST_INCREASE")
```
```kotlin
val intent = Intent("BROADCAST_INCREASE")
intent.putExtra("step", 2)
LocalBroadcastUtil.sendBroadcast(intent)
```
##### 注销广播接收器
如果在注册广播接收器的时候提供了`LifecycleOwner`参数，则无需再手动注销广播接收器，该接收器会在宿主的生命周期结束时自动注销。
```kotlin
LocalBroadcastUtil.unregisterReceiver(broadcastReceiver)
```

## 混淆说明
本项目对混淆无任何要求。
  
## 效果展示
![示例APP](https://resources.mydaydream.com/img/2020/06/09/024a05e1-c313-4698-9637-821905a46c1b.jpg)

## 国内文档地址
* https://www.jianshu.com/p/24db2b4b27da

## 维护计划
不定期进行功能更新，欢迎提出功能需求。