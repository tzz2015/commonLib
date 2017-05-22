package com.example.a11829.commonlib.contact;

import com.example.a11829.commonlib.base.BasePresenter;
import com.example.a11829.commonlib.base.BaseView;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;

/**
 * 杭州融科网络
 * 刘宇飞创建 on 2017/5/22.
 * 描述：
 */

public interface DemoActivityContact {
    interface View extends BaseView {
       void setView(UserInfoModel userInfoModel);

    }
    abstract   class Presenter extends BasePresenter<View> {
        public abstract void getData();
    }
}
