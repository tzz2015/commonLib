package com.zyf.fwms.commonlibrary.utils.statusbar.impl;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zyf.fwms.commonlibrary.utils.statusbar.IStatusBarFontHelper;
import com.zyf.fwms.commonlibrary.utils.statusbar.Rom;

/**
 * Created by kzl on 2016/5/17
 */
public class ColorOSHelper implements IStatusBarFontHelper {
    final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;
    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为ColorOS用户
     * @return boolean 成功执行返回true
     */
    @Override
    public boolean setStatusBarLightMode(Activity activity, boolean lightMode) {
        if(!Rom.isOppo()) return false;
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }

                int vis = window.getDecorView().getSystemUiVisibility();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (lightMode) {

                        vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                    } else {

                        vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                    }

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES. KITKAT) {

                    if (lightMode) {

                        vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;

                    } else {

                        vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;

                    }

                }

                window.getDecorView().setSystemUiVisibility(vis);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
