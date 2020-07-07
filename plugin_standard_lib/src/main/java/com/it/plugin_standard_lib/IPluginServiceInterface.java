package com.it.plugin_standard_lib;

import android.app.Service;
import android.content.Intent;

/**
 * Created by lgc on 2020-02-11.
 *
 * @description 定义插件化中服务的标准
 */
public interface IPluginServiceInterface {
    /**
     * 将宿主APP环境给到插件
     * @param service
     */
    void attachAppContext(Service service);

    int onStartCommand(Intent intent, int flags, int startId);

    public boolean onUnbind(Intent intent);

    public void onCreate();

    public void onDestroy();
}
