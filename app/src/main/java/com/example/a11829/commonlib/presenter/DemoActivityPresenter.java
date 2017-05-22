package com.example.a11829.commonlib.presenter;

import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.http.HttpPresenter;
import com.example.a11829.commonlib.http.HttpTaskListener;
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
        Map<String,Object> map=new HashMap<>();
        map.put("phone","15506200515");
        map.put("code","4986");
        map.put("userType","2");
        map.put("alias", "ffffffff_c7a8_3c15_0000_00004ca6b30b");
        map.put("source","APP");
        map.put("userId","215");
        HttpPresenter.getInstance()
                .setRequsetId(10)
                .setContext(mContext)
                .setObservable(httpTask.requestLogin(map))
                .setCallBack(this).create();
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
