package com.it.plugin_standard_lib;

import android.content.Context;
import android.content.Intent;

/**
 * Created by lgc on 2020-02-11.
 *
 * @description
 */
public interface IPluginReceiverInterface {
    public void onReceive(Context context, Intent intent);
}
