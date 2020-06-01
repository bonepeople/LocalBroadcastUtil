## LocalBroadcastUtil - 本地广播工具类

### 简介
---
LocalBroadcastUntil是一个本地广播的工具类，它可以使我们更方便的使用LocalBroadcastManager。

LocalBroadcastManager是安卓系统提供的本地广播管理类，使用这个类可以方便的发送本地广播传递数据且不受线程的影响(消息处理逻辑都在主线程中进行)，在不引入第三方库的情况下，LocalBroadcastManager是本地全局消息处理的最佳方式。

### 集成方法
---
使用Gradle构建工具集成(已针对androidX更新)：
```groovy
// android support 工程
dependencies {
    implementation 'com.bonepeople.android.support:LocalBroadcastUtil:1.2.1'
}
// androidX 工程
dependencies {
    implementation 'com.bonepeople.android.lib:LocalBroadcastUtil:1.2.1'
}
```

### 使用示例
---
* 初始化
  
  推荐在`Application`的`onCreate()`函数中进行初始化，初始化的过程中会保存一个`LocalBroadcastManager`对象用于之后的使用。
  
  **！未经初始化的工具类无法使用。**
  ```java
  public class App extends Application {
      @Override
      public void onCreate() {
          super.onCreate();
          LocalBroadcastUtil.init(this);
      }
  }
  ```
* 注册接收器
  
  使用`registerReceiver(LifecycleOwner, BroadcastReceiver, String...)`可以快捷的注册接收器，且无需担心内存泄漏，该receiver会在界面销毁的时候自动注销。

  **！Activity和Fragment均可作为LifecycleOwner参数使用**
  ```java
  // 创建广播接收器，用于处理广播消息
  BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 获取广播的action
                String action = intent.getAction();
                if (action == null)
                    return;
                // 根据广播的action进行事件的处理，此时处于主线程中
                switch (action) {
                    case Constants.BROADCAST_INCREASE:
                        Log.d(TAG, Constants.BROADCAST_INCREASE);
                        break;
                    case Constants.BROADCAST_INCREASE:
                        Log.d(TAG, Constants.BROADCAST_INCREASE);
                        break;
                }
            }
        };
  
  // 使用指定的actions注册广播接收器，并绑定至当前页面的生命周期
  LocalBroadcastUtil.registerReceiver(this, receiver, Constants.BROADCAST_INCREASE, Constants.BROADCAST_INCREASE);
  ```
  对于额外的广播筛选条件设置，可以使用`registerReceiver(LifecycleOwner, BroadcastReceiver, IntentFilter)`方法进行注册。
  
  如果在注册广播接收器的时候并未提供LifecycleOwner参数，在广播接收器使用完毕后需要在合适的时机调用`unregisterReceiver(BroadcastReceiver)`注销接收器。
  ```java
  // 如果receiver已绑定至界面的生命周期，则无需手动注销。
  LocalBroadcastUtil.unregisterReceiver(receiver)
  ```
* 发送广播
  
  如果是简单的消息广播，可直接调用`sendBroadcast(String)`发送广播，如果需要传递数据，需要调用`sendBroadcast(Intent)`发送广播。
  ```java
  // 使用指定的action发送广播
  LocalBroadcastUtil.sendBroadcast(Constants.BROADCAST_INCREASE);
  ```
* 更多详细的使用方法可以参考对应分支中的`simple`工程。
### 混淆说明
---
  本项目对混淆无任何要求。
