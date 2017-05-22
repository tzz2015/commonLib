package com.example.a11829.commonlib;

import android.app.Application;
import android.content.Context;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/5/19.
 * 描述：
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
       context=this;
    }
    public static Context getContext(){
        return context;
    }
}
