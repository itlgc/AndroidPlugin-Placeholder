package com.it.androidplugin_placeholder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * Created by lgc on 2020-02-10.
 *
 * @description
 */
public class PluginManager {
    public static final String TAG = PluginManager.class.getSimpleName();

    private static PluginManager instance;
    private Context context;

    private PluginManager(Context context) {
        this.context = context;
    }

    public static PluginManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context);
                }
            }
        }
        return instance;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    private DexClassLoader dexClassLoader;

    private Resources resources;

    public Resources getResources() {
        return resources;
    }

    /**
     * 加载插件apk  加载其中的组件的class 和布局layout
     */
    public void loadPlugin(String pluginPath) {

        try {
            //            File file = new File(Environment.getExternalStorageState() + File
            //            .separator + "plugin" +
            //                    ".apk");
            //            if (!file.exists()) {
            //                Log.d("TAG:" + TAG, "插件包不存在");
            //                return;
            //            }
            //            //获取插件包放置路径
            //            String pluginPath = file.getAbsolutePath();

            Log.d("TAG:" + TAG, "加载apk的地址：" + pluginPath);
            if (TextUtils.isEmpty(pluginPath)) {
                return;
            }
            /**
             * 加载插件里的 class
             */
            //dexClassLoader需要一个缓存目录， /data/data/当前应用包名/pluginDir 类似于SP
            File cacheDir = context.getDir("pluginDir", Context.MODE_PRIVATE);
            //File cacheDir = context.getCacheDir();


            dexClassLoader = new DexClassLoader(pluginPath, cacheDir.getAbsolutePath(),
                    null, context.getClassLoader());

            /**
             * 加载插件里的 layout
             */

            //加载资源
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath",
                    String.class);
            addAssetPathMethod.setAccessible(true);
            // public int addAssetPath(String path) 执行此方法将插件包路径传入进而加载里面的资源
            addAssetPathMethod.invoke(assetManager, pluginPath);

            //宿主APP的Resources信息
            Resources r = context.getResources();
            //这里的Resources 是加载插件包中的资源的Resources ，参数2、3 是资源配置信息，这里使用宿主的
            resources = new Resources(assetManager, r.getDisplayMetrics(),
                    r.getConfiguration());

            Log.d("TAG:" + TAG, "插件包加载成功");
            Toast.makeText(context, "插件包加载成功", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG:" + TAG, "插件包加载失败");
        }

    }


    //反射系统源码 来解析apk文件 获取文件里面所有的信息  目的：获取插件包配置文件中静态广播信息，在宿主APP这边帮其注册
    public  void parserApkAction() {

        try {

            //获取插件包放置路径(/data/data/当前应用包名/pluginDir/....apk)
            File file = new File(context.getDir("pluginDir",Context.MODE_PRIVATE), "plugin" +
                    ".apk");
            if (!file.exists()) {
                Log.d("TAG:" + TAG, "插件包不存在");
                return;
            }
//            String pluginPath = file.getAbsolutePath();



            //实例化PackageParser对象
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object mPackageParser = packageParserClass.newInstance();

            //1、执行系统源码中 public Package parsePackage(File packageFile, int flags) 获取package信息
            Method parsePackageMethod = packageParserClass.getMethod("parsePackage", File.class,
                    int.class);
            Object mPackage = parsePackageMethod.invoke(mPackageParser, file,
                    PackageManager.GET_ACTIVITIES);

            //继续分析PackageParser$Package  获取receivers
            Field receiversField = mPackage.getClass().getField("receivers");
            //receivers 本质上是一个ArrayList集合
            Object receivers = receiversField.get(mPackage);
            ArrayList receiversList = (ArrayList) receivers;

            if (receiversList == null) {
                Log.d("TAG:" + TAG, "Manifest中没有配置静态广播receiver");
                return;
            }
            //这里的Activity是 PackageParser的内部类
            for (Object mActivity : receiversList) {
                //activity:<receiver android:name=".TestStaticReceiver" >

                //获取到 filter: <intent-filter> 集合
                Class<?> mComponentClass = Class.forName("android.content.pm.PackageParser$Component");

                Field intentsField = mComponentClass.getField("intents");
                Object intents = intentsField.get(mActivity);
                ArrayList<IntentFilter> intentsList = (ArrayList) intents;

                //2、获取要注册的静态广播的全类名信息
                // PackageItemInfo.name   ActivityInfo.name==  android:name=".TestStaticReceiver"

                //通过执行 public static final ActivityInfo generateActivityInfo(Activity a, int flags,
                // PackageUserState state, int userId) 来获取ActivityInfo

                //准备相关方法参数
                Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
                Object mPackageUserState = packageUserStateClass.newInstance();
                Class<?> userHandleClass = Class.forName("android.os.UserHandle");
                Method getCallingUserIdMethod = userHandleClass.getMethod("getCallingUserId");
                int userId = (int) getCallingUserIdMethod.invoke(null);


                Method generateActivityInfoMethod = packageParserClass.getMethod(
                        "generateActivityInfo",mActivity.getClass(), int.class,
                        packageUserStateClass, int.class);
                //静态方法反射时不需要给对象
                ActivityInfo mActivityInfo = (ActivityInfo) generateActivityInfoMethod.invoke(null, mActivity
                        ,0, mPackageUserState, userId);

                if (mActivityInfo != null) {
                    String receiverClassName = mActivityInfo.name;
                    Class<?> receiverClass = getDexClassLoader().loadClass(receiverClassName);
                    BroadcastReceiver broadcastReceiver = (BroadcastReceiver) receiverClass.newInstance();
                    for (IntentFilter intentFilter : intentsList) {
                        //注册静态广播
                        context.registerReceiver(broadcastReceiver, intentFilter);
                        Log.d("TAG:" + TAG, "插件中静态广播注册成功 ：" + receiverClassName);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
