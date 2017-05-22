package com.example.a11829.commonlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.a11829.commonlib.base.BaseActivity;
import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.databinding.ActivityDemoBinding;
import com.example.a11829.commonlib.presenter.DemoActivityPresenter;
import com.zyf.fwms.commonlibrary.model.RxCodeConstants;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;
import com.zyf.fwms.commonlibrary.utils.RxBus;

import rx.Subscription;
import rx.functions.Action1;

public class DemoActivity extends BaseActivity<DemoActivityPresenter,ActivityDemoBinding> implements View.OnClickListener,DemoActivityContact.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        hideBackImg();
        setTitle("demo首页");
        initRxBus();
        bindingView.tvRecyclerView.setOnClickListener(this);
        bindingView.tvRequest.setOnClickListener(this);
        bindingView.tvRxBus.setOnClickListener(this);
        bindingView.tvRxPermission.setOnClickListener(this);
    }

    @Override
    protected void initPresenter() {
      mPresenter.setView(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_recyclerView:
                intent=new Intent(context,MainActivity.class);
                break;
            case R.id.tv_request://请求网络数据
                mPresenter.getData();
                break;
            case R.id.tv_rx_bus://发送消息可在任何地方
                RxBus.getDefault().post(RxCodeConstants.SHOW_TOAST,2);
                break;
            case R.id.tv_rx_permission://y异步获取权限
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }


    /**
     * 请求网络完成回显
     * @param userInfoModel
     */
    @Override
    public void setView(UserInfoModel userInfoModel) {
        bindingView.setUserInfo(userInfoModel);
    }

    /**
     * 在任何界面出发操作 这里响应
     */
    @Override
    public void initRxBus() {
        Subscription subscribe = RxBus.getDefault().toObservable(RxCodeConstants.SHOW_TOAST, Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Toast.makeText(context,"首页收到消息",Toast.LENGTH_SHORT).show();
                    }
                });
        addSubscription(subscribe);
    }
}
