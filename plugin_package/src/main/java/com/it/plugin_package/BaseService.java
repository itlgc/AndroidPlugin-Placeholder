package com.it.plugin_package;

import android.annotation.SuppressLint;
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
public class BaseService extends Service implements IPluginServiceInterface {


    protected Service service;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attachAppContext(Service service) {
        this.service = service;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
