package com.example.a11829.commonlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.a11829.commonlib.base.BaseActivity;
import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.databinding.ActivityDemoBinding;
import com.example.a11829.commonlib.presenter.DemoActivityPresenter;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;

public class DemoActivity extends BaseActivity<DemoActivityPresenter,ActivityDemoBinding> implements View.OnClickListener,DemoActivityContact.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        hideBackImg();
        setTitle("demo首页");
        bindingView.tvRecyclerView.setOnClickListener(this);
        bindingView.tvRequest.setOnClickListener(this);
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
}
