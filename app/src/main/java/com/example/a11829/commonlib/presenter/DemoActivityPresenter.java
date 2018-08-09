package com.example.a11829.commonlib.presenter;

import com.example.a11829.commonlib.MyApplication;
import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.http.HttpPresenter;
import com.example.a11829.commonlib.http.HttpTaskListener;
import com.example.a11829.commonlib.model.AccountInputModel;
import com.google.gson.JsonObject;
import com.zyf.fwms.commonlibrary.http.BaseRespose;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.JmUtils;
import com.zyf.fwms.commonlibrary.utils.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 刘宇飞创建 on 2017/5/22.
 * 描述：
 */

public class DemoActivityPresenter extends DemoActivityContact.Presenter implements HttpTaskListener {
    @Override
    public void getData() {

       /* HttpPresenter.getInstance()
                .setRequsetId(10)   //请求id 非必须
                .setContext(mContext)//
                .setObservable(httpTask.requestLogin())  //必须
                .setCallBack(this)
                .create();*/

        Subscription subscribe = httpTask.requestLogin()
                .subscribeOn(Schedulers.newThread())//请求网络在子线程中
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程中
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }


                    @Override
                    public void onNext(JsonObject jsonObject) {
                        String data = jsonObject.get("data").getAsString();
                        LogUtil.getInstance().e(JmUtils.jm(data ));
                    }


                });
    }



    @Override
    public void onSuccess(int requestId, Object o) {
        switch (requestId){
            case 10://网络请求完成
               String jmString= (String) o;
            try {
                LogUtil.getInstance().e(JmUtils.jm(jmString));

            }catch (Exception e){
                LogUtil.getInstance().e(e.getMessage());
            }
                break;
        }
    }

    @Override
    public void onException(int requestId) {


    }
}
