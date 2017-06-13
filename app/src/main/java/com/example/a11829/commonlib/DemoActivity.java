package com.example.a11829.commonlib;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.a11829.commonlib.base.BaseActivity;
import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.databinding.ActivityDemoBinding;
import com.example.a11829.commonlib.presenter.DemoActivityPresenter;
import com.jph.takephoto.model.TImage;
import com.zyf.fwms.commonlibrary.model.RxCodeConstants;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;
import com.zyf.fwms.commonlibrary.photo.PhotoModel;
import com.zyf.fwms.commonlibrary.utils.LogUtil;
import com.zyf.fwms.commonlibrary.utils.RxBus;

import java.util.List;

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
        mBindingView.tvRecyclerView.setOnClickListener(this);
        mBindingView.tvRequest.setOnClickListener(this);
        mBindingView.tvRxBus.setOnClickListener(this);
        mBindingView.tvRxPermission.setOnClickListener(this);
        mBindingView.tvWb.setOnClickListener(this);
        mBindingView.tvPhoto.setOnClickListener(this);
    }

    @Override
    protected void initView() {

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
                intent=new Intent(mContext,MainActivity.class);
                break;
            case R.id.tv_request://请求网络数据
                mPresenter.getData();
                break;
            case R.id.tv_rx_bus://发送消息可在任何地方
                RxBus.getDefault().post(RxCodeConstants.SHOW_TOAST,"发送消息可在任何地方");
                break;
            case R.id.tv_rx_permission://y异步获取权限
                requestPermission(new String[]{Manifest.permission.CAMERA});
                break;
            case R.id.tv_wb://隐藏标题
                intent=new Intent(mContext,NoTitleActivity.class);
                break;
            case R.id.tv_photo://拍照
                takePhoto();
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }

    private void takePhoto() {
        PhotoModel photoModel=new PhotoModel(this);
        photoModel.isCrop=false;
        photoModel.maxSize=9;
        photoModel.setCallback(new PhotoModel.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(List<TImage> resultList) {
                showToast("返回照片数据看log");
                for(TImage model:resultList){
                    LogUtil.getInstance().e(model.getCompressPath());
                }
            }
        });
        photoModel.initTakePhoto();
    }

    /**
     * 授权回调
     * @param aBoolean
     */
    @Override
    protected void requestPermissionCallBack(Boolean aBoolean) {
        super.requestPermissionCallBack(aBoolean);
    }

    /**
     * 请求网络完成回显
     * @param userInfoModel
     */
    @Override
    public void setView(UserInfoModel userInfoModel) {
        mBindingView.setUserInfo(userInfoModel);
    }

    /**
     * 在任何界面出发操作 这里响应
     */
    @Override
    public void initRxBus() {
        Subscription subscribe = RxBus.getDefault().toObservable(RxCodeConstants.SHOW_TOAST, String.class)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        showToast("首页收到消息---："+s);

                    }
                });
        addSubscription(subscribe);



    }


}
