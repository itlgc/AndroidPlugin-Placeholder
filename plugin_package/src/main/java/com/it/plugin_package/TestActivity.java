package com.it.plugin_package;

import android.os.Bundle;

/**
 * Created by lgc on 2020-02-10.
 *  插件内部的Activity
 * @description
 */
public class TestActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test);
    }
}
