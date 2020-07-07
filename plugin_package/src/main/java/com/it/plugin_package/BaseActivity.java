package com.it.plugin_package;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import com.it.plugin_standard_lib.IPluginActivityInterface;
/**
 * Created by lgc on 2020-02-09.
 *
 * @description
 */
public class BaseActivity extends Activity implements IPluginActivityInterface {


    protected Activity appContext;

    @Override
    public void attachAppContext(Activity activity) {

        this.appContext = activity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    public void setContentView(int layoutResId){
        appContext.setContentView(layoutResId);
    }

    public View findViewById(int resId) {
       return appContext.findViewById(resId);
    }

    public void startActivity(Intent intent) {
        Intent intentNew = new Intent();
        //传递TestActivity.class 的全类名
        intentNew.putExtra("className", intent.getComponent().getClassName());
        appContext.startActivity(intentNew);
    }

    public ComponentName startService(Intent intent) {
        Intent intentNew = new Intent();
        intentNew.putExtra("className", intent.getComponent().getClassName());//TestService 全类名
        return appContext.startService(intentNew);
    }

    //使用宿主APP环境 注册广播
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return appContext.registerReceiver(receiver, filter);
    }

    //使用宿主APP环境 发送广播
    public void sendBroadcast(Intent intent) {
        appContext.sendBroadcast(intent);
    }
}
