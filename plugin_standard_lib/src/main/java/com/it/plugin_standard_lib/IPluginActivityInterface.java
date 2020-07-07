package com.it.plugin_standard_lib;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lgc on 2020-02-09.
 *
 * @description
 */
public interface IPluginActivityInterface {

    /**
     * 将宿主APP环境给到插件
     * @param activity
     */
    void attachAppContext(Activity activity);
    //生命周期方法
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onDestroy();

    //...
}
