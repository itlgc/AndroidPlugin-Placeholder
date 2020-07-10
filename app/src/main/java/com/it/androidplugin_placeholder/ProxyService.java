package com.it.androidplugin_placeholder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.it.plugin_standard_lib.IPluginServiceInterface;

import androidx.annotation.Nullable;

/**
 * Created by lgc on 2020-02-11.
 *
 * @description
 */
public class ProxyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String className = intent.getStringExtra("className");
        try {
            //加载插件包apk中的 com.it.plugin_package.TestService
            Class<?> TestServiceClass =
                    PluginManager.getInstance(this).getDexClassLoader().loadClass(className);
            Object TestService = TestServiceClass.newInstance();
            IPluginServiceInterface serviceInterface = (IPluginServiceInterface) TestService;

            //注入组件环境
            serviceInterface.attachAppContext(this);
            //真正启动插件中的Service
            serviceInterface.onStartCommand(intent, flags, startId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

}
