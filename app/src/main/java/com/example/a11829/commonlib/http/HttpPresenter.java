package com.example.a11829.commonlib.http;

import android.content.Context;

import com.example.a11829.commonlib.MyApplication;
import com.example.xrecyclerview.CheckNetwork;
import com.zyf.fwms.commonlibrary.http.BaseRespose;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.LogUtil;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/5/20.
 * 描述：
 */

public class HttpPresenter<T>  implements HttpBuilder<T> {
    private Observable observable;
    private int  requstId;
    private HttpTaskListener<T> listener;
    private Context context;
    private static volatile HttpPresenter httpPresenter;

    public static HttpPresenter getInstance(){
        if(httpPresenter==null){
            synchronized (HttpPresenter.class){
                if(httpPresenter==null){
                    httpPresenter=new HttpPresenter();
                }
            }
        }
        return httpPresenter;
    }

    private Subscription request() {
             //避免没有必要的请求
            if(CheckNetwork.isNetworkConnected(MyApplication.getContext())){
                CommonUtils.showToast(MyApplication.getContext(),"无网络连接,请检查网络");
                return null;
            }
            if(context!=null){
                CommonUtils.getInstance().showInfoProgressDialog(context);
            }
            Subscription subscribe = observable
                    .subscribeOn(Schedulers.newThread())//请求网络在子线程中
                    .observeOn(AndroidSchedulers.mainThread())//回调在主线程中
                    .subscribe(new Subscriber<BaseRespose<T>>() {
                        @Override
                        public void onCompleted() {
                         CommonUtils.getInstance().hideInfoProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                             CommonUtils.getInstance().hideInfoProgressDialog();
                            if(listener!=null){
                                listener.onException(requstId);
                            }

                        }

                        @Override
                        public void onNext(BaseRespose<T> baseResponseVo) {
                            CommonUtils.getInstance().hideInfoProgressDialog();
                            if(listener!=null){
                                listener.onSuccess(requstId,baseResponseVo.data);
                            }


                        }
                    });
            //activity 或者fragment销毁时 必须销毁所有的请求 不然容易导致空指针
            CommonUtils.getInstance().addSubscription(subscribe);

            return subscribe;





    }

    @Override
    public HttpBuilder setRequsetId(int requsetId) {
        this.requstId=requsetId;
        return this;
    }

    @Override
    public HttpBuilder setContext(Context context) {
        this.context=context;
        return this;
    }

    @Override
    public HttpBuilder setObservable(Observable observable) {
        this.observable=observable;
        return this;
    }
    @Override
    public HttpBuilder setCallBack(HttpTaskListener listener) {
        this.listener=listener;
        return this;
    }

    @Override
    public Subscription create() {
        if(observable==null){
            LogUtil.getInstance().e("请设置observable");
            return null;
        }else {
           return request();
        }

    }


}
