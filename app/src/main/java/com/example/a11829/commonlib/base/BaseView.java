package com.example.a11829.commonlib.base;

/**
 * 公司：
 * 刘宇飞 创建 on 2017/3/6.
 * 描述：view 基类
 */

public interface BaseView {
    /**
     * 显示加载动画
     */
    void showInfoProgressDialog();

    /**
     * 隐藏加载动画
     */
    void hideInfoProgressDialog();

    /**
     * 显示网络错误，modify 对网络异常在 BaseActivity 和 BaseFragment 统一处理
     */
    void showNetError();
    /**
     * 隐藏网络错误
     */
    void hideNetError();

}
