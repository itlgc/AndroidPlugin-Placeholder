package com.it.androidplugin_placeholder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.it.plugin_standard_lib.IPluginReceiverInterface;


/**
 * Created by lgc on 2020-02-11.
 *
 * @description 代理广播  真正接收消息的
 * （在插件内动态注册的广播发送消息，实际上还是要到有环境的代理广播这里接收，然后再传递到插件广播中）
 */
class ProxyReceiver extends BroadcastReceiver {

    //TestReceiver全类名
    private String className;

    public ProxyReceiver(String className) {

        this.className = className;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Class<?> testReceiverClass =
                    PluginManager.getInstance(context).getDexClassLoader().loadClass(className);
            Object testReceiver = testReceiverClass.newInstance();
            IPluginReceiverInterface receiverInterface = (IPluginReceiverInterface) testReceiver;

            //插件中的广播类有实现标准接口 IPluginReceiverInterface，所以通过强转成这个接口去调用到插件中的接收方法
            receiverInterface.onReceive(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
