package com.it.androidplugin_placeholder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 占位式插件化设计
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private String pluginApkPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_load).setOnClickListener(this);
        findViewById(R.id.btn_start_plugin_activity).setOnClickListener(this);
        findViewById(R.id.btn_load_plugin_static_receiver).setOnClickListener(this);
        findViewById(R.id.btn_send_receiver).setOnClickListener(this);

        //将插件apk拷贝到APP缓存目录下
         pluginApkPath = Utils.copyAssetsAndWrite(this, "plugin_package.apk",false);
        findViewById(R.id.btn_load).performClick();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                //加载插件
                PluginManager.getInstance(this).loadPlugin(pluginApkPath);
                break;
            case R.id.btn_start_plugin_activity:
                //启动插件里的Activity  //利用占位  代理Activity来实现
                //获取插件包里的Activity
                PackageManager packageManager = getPackageManager();
                PackageInfo packageArchiveInfo =
                        packageManager.getPackageArchiveInfo(pluginApkPath, PackageManager.GET_ACTIVITIES);
                if (packageArchiveInfo == null) {
                    Log.e("TAG:" + TAG, "获取插件包信息失败");
                    return;
                }
                ActivityInfo activity = packageArchiveInfo.activities[0];
                Log.d("TAG:" + TAG, "获取插件Activity name :" + activity.name);

                //跳转到 代理/占位Activity
                Intent intent = new Intent(this, ProxyActivity.class);
                intent.putExtra("className", activity.name);
                startActivity(intent);

                break;
            case R.id.btn_load_plugin_static_receiver:
                //通过解析插件包，注册其中的静态广播
                PluginManager.getInstance(this).parserApkAction();
                break;
            case R.id.btn_send_receiver:
                //发送静态广播
                Intent intentReceiver = new Intent();
                intentReceiver.setAction("plugin.static_receiver");//在插件包Manifest中定义了
                sendBroadcast(intentReceiver);
                break;
            default:

                break;
        }
    }
}
