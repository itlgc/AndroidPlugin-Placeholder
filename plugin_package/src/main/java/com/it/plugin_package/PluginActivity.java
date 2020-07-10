package com.it.plugin_package;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 插件包入口Activity
 */
public class PluginActivity extends BaseActivity {

    public static final String ACTION = "com.it.plugin_package.action";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //需要依靠宿主的环境
        super.setContentView(R.layout.activity_plugin);

        String msg = savedInstanceState.getString("msg");
        //注意： 这里不能使用this ，因为插件包没有安装，没有组件环境
        Toast.makeText(appContext, "收到消息：" + msg, Toast.LENGTH_SHORT).show();

        //插件中跳转Activity
//        findViewById(R.id.btn_start_activity);
        super.findViewById(R.id.btn_start_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, TestActivity.class);
//                startActivity(intent);
                PluginActivity.super.startActivity(intent);
            }
        });

        //插件中开启Service
        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, TestService.class);
                startService(intent);
            }
        });


        //插件内部注册插件的广播接受者（动态注册）
        findViewById(R.id.btn_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION);
                registerReceiver(new TestReceiver(), intentFilter);
            }
        });

        //插件内发送广播 （动态注册的广播发送）
        findViewById(R.id.btn_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });

    }
}
