package com.it.androidplugin_placeholder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import com.it.plugin_standard_lib.IPluginActivityInterface;

import androidx.annotation.Nullable;

/**
 * Created by lgc on 2020-02-10.
 *
 * @description 用于代理/占位 插件里的Activity
 */
public class ProxyActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取得插件包中的Activity class
        String className = getIntent().getStringExtra("className");
        try {
            //实例化插架包中的Activity
            Class<?> pluginActivityClassName = getClassLoader().loadClass(className);
            Object pluginActivity = pluginActivityClassName.newInstance();
            IPluginActivityInterface iPluginActivityInterface = (IPluginActivityInterface) pluginActivity;

            //注入环境
            iPluginActivityInterface.attachAppContext(this);

            Bundle bundle = new Bundle();
            bundle.putString("msg","我是宿主APP传递过来的信息");
            iPluginActivityInterface.onCreate(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getDexClassLoader();
    }


    //为了实现插件包中Activity内部跳转，最终会由插件包中BaseActivity调用到这里
    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");

        //要给 TestActivity进栈
        //跳ProxyActivity 进入到onCreate() 执行启动TestActivity
        Intent proxyIntent = new Intent(this, ProxyActivity.class);
        proxyIntent.putExtra("className", className);
        super.startActivity(proxyIntent);
    }

    @Override
    public ComponentName startService(Intent service) {
        String className = service.getStringExtra("className");
        Intent proxyIntent = new Intent(this, ProxyService.class);
        proxyIntent.putExtra("className", className);
        return super.startService(proxyIntent);
    }


    //（在插件内动态注册的广播注册，实际上还是要到有环境的代理广播这里注册）
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        String className = receiver.getClass().getName();//TestReceiver全类名
        return super.registerReceiver(new ProxyReceiver(className), filter);

    }

    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }
}
