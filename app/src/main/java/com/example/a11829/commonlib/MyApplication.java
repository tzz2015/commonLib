package com.example.a11829.commonlib;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.zyf.fwms.commonlibrary.utils.LogUtil;

/**
 *
 * 刘宇飞 创建 on 2017/5/19.
 * 描述：
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
       context=this;
       initX5();
     //  CrashHandler.getInstance().init(context);
    }
    public static Context getContext(){
        return context;
    }

    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                LogUtil.getInstance().e(" 初始化腾讯浏览器 is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                LogUtil.getInstance().e( "下载完成");
            }

            @Override
            public void onInstallFinish(int i) {
                LogUtil.getInstance().e("安装完成");
            }

            @Override
            public void onDownloadProgress(int i) {
                LogUtil.getInstance().e( "下载进度:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
