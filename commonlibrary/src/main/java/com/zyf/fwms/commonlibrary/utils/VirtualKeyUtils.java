package com.zyf.fwms.commonlibrary.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;

import com.zyf.fwms.commonlibrary.http.HttpUtils;

import java.lang.reflect.Method;

/**
 * 创建 by lyf on 2018/2/5.
 * 描述：
 */

public class VirtualKeyUtils {
    private View decorView;


    public static VirtualKeyUtils instance;

    public static VirtualKeyUtils getInstance() {
        if (instance == null) {
            synchronized (VirtualKeyUtils.class) {
                if (instance == null) {
                    instance = new VirtualKeyUtils();
                }
            }
        }
        return instance;
    }

    public void init(Activity activity) {
        //获取顶层视图
        decorView = activity.getWindow().getDecorView();
        int flag = checkVirualkey(activity);
        // 获取属性
        if (flag > 0) decorView.setSystemUiVisibility(flag);

    }

    /**
     * 检查是否有虚拟键
     */
    public int checkVirualkey(Activity activity) {
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
               // | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
        if (Build.VERSION.SDK_INT < 19 || !checkDeviceHasNavigationBar(activity))
            return 0;
        else return flag;

    }

    /**
     * 判断是否存在虚拟按键
     *
     * @return
     */
    private boolean checkDeviceHasNavigationBar(Activity activity) {
        boolean hasNavigationBar = false;
        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }


}
