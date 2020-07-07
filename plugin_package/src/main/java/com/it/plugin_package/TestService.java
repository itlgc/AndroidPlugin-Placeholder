package com.it.plugin_package;


import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by lgc on 2020-02-11.
 *
 * @description 插件中的Service
 */
public class TestService extends BaseService {
    public static final String TAG = TestService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(service.getApplicationContext(),"插件中的Service开启了", Toast.LENGTH_SHORT).show();
        //模拟耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep(1000);
                    Log.d("TAG:" + TAG, "正在执行插件中Service...");
                }

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
