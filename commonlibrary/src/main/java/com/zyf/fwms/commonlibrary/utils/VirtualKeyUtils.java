package com.zyf.fwms.commonlibrary.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 创建 by lyf on 2018/2/5.
 * 描述：
 */

public class VirtualKeyUtils {

    private static VirtualKeyUtils instance;
    public static VirtualKeyUtils getInstance(){
        if(instance==null){
            synchronized (VirtualKeyUtils.class) {
                if (instance == null) {
                    instance = new VirtualKeyUtils();
                }
            }
        }
        return instance;
    }


    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    public void init(View content) {
        mChildOfContent = content;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    public void possiblyResizeChildOfContent() {
        if(mChildOfContent==null) return;
        int usableHeightNow = computeUsableHeight();
        //int usableHeightNow = AutoUtils.displayHeight;

        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致


//            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            if (heightDifference > (usableHeightSansKeyboard / 4)) {
//                // keyboard probably just became visible
//                isKeyBordVisiable=true;
//            } else {
//                // keyboard probably just became hidden
//                isKeyBordVisiable=false;
//            }
            //将计算的可视高度设置成视图的高度
            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 用于隐藏状态栏的页面
     */
    public void noBarResizeChildOfContent(){
        if(mChildOfContent==null) return;
        mChildOfContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayoutParams.height = AutoUtils.displayHeight;
                mChildOfContent.requestLayout();//请求重新布局
            }
        },300);

    }

    private int computeUsableHeight() {
        //计算视图可视高度
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
}
