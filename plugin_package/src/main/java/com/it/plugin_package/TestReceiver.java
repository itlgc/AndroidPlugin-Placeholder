package com.it.plugin_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.it.plugin_standard_lib.IPluginReceiverInterface;
/**
 * Created by lgc on 2020-02-11.
 *
 * @description
 */
public class TestReceiver extends BroadcastReceiver implements IPluginReceiverInterface {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(),"我是插件里的广播接收者，我收到广播了", Toast.LENGTH_SHORT).show();
    }
}
