package com.example.a11829.commonlib.base;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.a11829.commonlib.http.HttpTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zyf.fwms.commonlibrary.R;
import com.zyf.fwms.commonlibrary.databinding.ActivityBaseBinding;
import com.zyf.fwms.commonlibrary.http.HttpUtils;
import com.zyf.fwms.commonlibrary.model.AccountInfo;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;
import com.zyf.fwms.commonlibrary.utils.AutoUtils;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.SharedPreUtil;
import com.zyf.fwms.commonlibrary.utils.StatusBarUtil;
import com.zyf.fwms.commonlibrary.utils.TUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 *
 * 刘宇飞创建 on 2017/5/18.
 * 描述：
 */
public abstract class BaseActivity<E extends BasePresenter,SV extends ViewDataBinding> extends FragmentActivity {
    //布局view
    protected SV bindingView;
    private ActivityBaseBinding mBaseBinding;
    private CompositeSubscription mCompositeSubscription;
    protected Context context;
    protected HttpTask httpTask;
    public E mPresenter;

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        httpTask = HttpUtils.getInstance().createRequest(HttpTask.class);
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.mContext = this;
            mPresenter.httpTask = httpTask;
        }
        initPresenter();

    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);
        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = mBaseBinding.container;
        mContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());
        // 设置透明状态栏
        StatusBarUtil.setColor(this, CommonUtils.getColor(this,R.color.colorTitle),0);
        initLisener();
        context=this;
        //移动到application
        AutoUtils.setSize(this, false, 720, 1280);
        //自适应页面
        AutoUtils.auto(this);



    }

    protected abstract void initPresenter();


    private void initLisener() {
        mBaseBinding.llErrorRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        mBaseBinding.commonTitle.llLiftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 隐藏标题栏
     */
      protected void hideTitleBar(){
          mBaseBinding.commonTitle.rlTitleBar.setVisibility(View.GONE);
      }
    /**
     * 隐藏返回箭头
     */
    protected void hideBackImg(){
        mBaseBinding.commonTitle.llLiftBack.setVisibility(View.GONE);
    }
    /**
     * 设置标题
     */
     protected void setTitle(String title){
         mBaseBinding.commonTitle.tvTitle.setText(CommonUtils.isNotEmpty(title)?title:"");
     }
    /**
     * 设置右侧文字
     */
    protected void setRightTitle(String rightTitle, View.OnClickListener listener){
        mBaseBinding.commonTitle.tvRightText.setText(CommonUtils.isNotEmpty(rightTitle)?rightTitle:"");
        mBaseBinding.commonTitle.llRightText.setVisibility(View.VISIBLE);
        mBaseBinding.commonTitle.llRightImg.setVisibility(View.GONE);
        if(listener!=null){
            mBaseBinding.commonTitle.llRightText.setOnClickListener(listener);
        }
    }
    /**
     * 设置右侧图片
     */
    protected void setRightImg(int img, View.OnClickListener listener){
        mBaseBinding.commonTitle.llRightText.setVisibility(View.GONE);
        mBaseBinding.commonTitle.llRightImg.setVisibility(View.VISIBLE);
        if(img>0){
            mBaseBinding.commonTitle.ivRightImg.setImageResource(img);
        }
        if(listener!=null){
            mBaseBinding.commonTitle.llRightImg.setOnClickListener(listener);
        }
    }
    /**
     * 设置右侧图片
     */
    protected void setRightImg(int img, View.OnClickListener listener,float width,float height){
        mBaseBinding.commonTitle.llRightText.setVisibility(View.GONE);
        mBaseBinding.commonTitle.llRightImg.setVisibility(View.VISIBLE);
        if(img>0){
            mBaseBinding.commonTitle.ivRightImg.setImageResource(img);
        }
        if(listener!=null){
            mBaseBinding.commonTitle.llRightImg.setOnClickListener(listener);
        }
        if(width>0&&height>0){
            ViewGroup.LayoutParams layoutParams = mBaseBinding.commonTitle.ivRightImg.getLayoutParams();
            layoutParams.width=CommonUtils.dip2px(context,width);
            layoutParams.height=CommonUtils.dip2px(context,height);
            mBaseBinding.commonTitle.ivRightImg.setLayoutParams(layoutParams);
        }
    }

    /**
     * 失败后点击刷新
     */
    protected void onRefresh() {

    }

    /**
     *显示toast
     * @param title
     */
    protected   void showToast(String title) {
      CommonUtils.showToast(context,title);
    }
    private KProgressHUD mProgressDialog;

    /**
     * 显示进度框
     * @param str
     */
    public final void showInfoProgressDialog(final String... str) {
        if (mProgressDialog == null) {
            mProgressDialog = new KProgressHUD(this);
            mProgressDialog.setCancellable(true);
        }
        if (str.length == 0) {
            mProgressDialog.setLabel("加载中...");
        } else {
            mProgressDialog.setLabel(str[0]);
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }
    /**
     * 隐藏等待条
     */
    public final void hideInfoProgressDialog() {
        CommonUtils.getInstance().hideInfoProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 显示错误页面或正常页面
     */
    protected void showErroView(boolean isShow){
        if(isShow){
            mBaseBinding.llErrorRefresh.setVisibility(View.VISIBLE);
            bindingView.getRoot().setVisibility(View.GONE);
        }else {
            mBaseBinding.llErrorRefresh.setVisibility(View.GONE);
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 添加网络请求观察者
     * @param s
     */
    public void addSubscription(Subscription s) {
        if(s==null){
            return;
        }
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    /**
     * 移除网络请求
     */
    public void removeSubscription() {
        CommonUtils.getInstance().removeSubscription();
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //发生异常 恢复用户信息
        if (AccountInfo.id == 0) {
            String userInfoModel = SharedPreUtil.getString(this, "UserInfoModel", "");
            if (CommonUtils.isNotEmpty(userInfoModel)) {
                UserInfoModel model = CommonUtils.getGson().fromJson(userInfoModel, UserInfoModel.class);
                model.setAccountInfo();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideInfoProgressDialog();
        removeSubscription();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
