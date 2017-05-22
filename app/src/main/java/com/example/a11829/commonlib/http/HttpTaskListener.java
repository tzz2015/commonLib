package com.example.a11829.commonlib.http;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/5/20.
 * 描述：
 */

public interface HttpTaskListener<T> {
    void onSuccess(int requestId, T t);
    void onException(int requestId);
}
