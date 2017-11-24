package com.iftech.car.common;


import android.app.Application;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.multidex.MultiDex;
import com.baidu.mapapi.SDKInitializer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * 
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */
public class MyApplication extends Application {
    public AppBarLayout appBarLayout;

    @Override
    public void onCreate() {
        super.onCreate();

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        SDKInitializer.initialize(this);
        ZXingLibrary.initDisplayOpinion(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
