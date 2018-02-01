package com.example.a11829.commonlib.presenter;

import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.http.HttpPresenter;
import com.example.a11829.commonlib.http.HttpTaskListener;
import com.example.a11829.commonlib.model.AccountInputModel;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 刘宇飞创建 on 2017/5/22.
 * 描述：
 */

public class DemoActivityPresenter extends DemoActivityContact.Presenter implements HttpTaskListener {
    @Override
    public void getData() {

        HttpPresenter.getInstance()
                .setRequsetId(10)   //请求id 非必须
                .setContext(mContext)//
                .setObservable(httpTask.requestLogin(new AccountInputModel("17605887136","123456")))  //必须
                .setCallBack(this)  //回调 非必须
                .create();
    }



    @Override
    public void onSuccess(int requestId, Object o) {
        switch (requestId){
            case 10://网络请求完成
                UserInfoModel userInfoModel= (UserInfoModel) o;
                mView.setView(userInfoModel);
                break;
        }
    }

    @Override
    public void onException(int requestId) {


    }
}
