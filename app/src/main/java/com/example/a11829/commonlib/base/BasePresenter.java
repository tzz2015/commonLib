package com.example.a11829.commonlib.base;


import com.example.a11829.commonlib.http.HttpTask;

/**
 * 公司：杭州融科网络科技
 * 刘宇飞 创建 on 2017/3/6.
 * 描述：p 基类
 */

public abstract class BasePresenter<T>{
    public BaseActivity mContext;
    public T mView;
    public HttpTask httpTask;


    public void setView(T v) {
        this.mView = v;
        this.onStart();
    }


    public void onStart(){
    };
    public void onDestroy() {
         mView=null;

    }
}