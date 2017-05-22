package com.example.a11829.commonlib.http;

import android.content.Context;

import rx.Observable;
import rx.Subscription;

/**
 *
 * 刘宇飞 创建 on 2017/5/20.
 * 描述：
 */

public interface HttpBuilder<T> {
    HttpBuilder setRequsetId(int requsetId);

    HttpBuilder setContext(Context context);

    HttpBuilder setObservable(Observable observable);

    Subscription create();

    HttpBuilder  setCallBack(HttpTaskListener<T> listener);

}
