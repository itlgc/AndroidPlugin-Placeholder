package com.it.plugin_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * Created by lgc on 2020-02-12.
 *
 * @description 插件内静态广播
 */
public class TestStaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"我是插件里的静态广播，我收到消息了", Toast.LENGTH_SHORT).show();
    }
}
