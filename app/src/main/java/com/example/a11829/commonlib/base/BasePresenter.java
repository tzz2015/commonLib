package com.example.a11829.commonlib.base;


import android.content.Context;

import com.example.a11829.commonlib.http.HttpTask;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 公司：
 * 刘宇飞 创建 on 2017/3/6.
 * 描述：p 基类
 */

public abstract class BasePresenter<T> {
    public Context mContext;
    public T mView;
    public HttpTask httpTask;
    protected Reference<T> mViewRef;


    public void setView(T v) {
        this.mView = v;
        this.onStart();
        mViewRef = new WeakReference<T>(v);
    }


    public void onStart() {

    }


    public void onDestroy() {
        httpTask = null;
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }

    }
}