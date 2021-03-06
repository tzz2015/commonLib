package com.example.a11829.commonlib.base;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.a11829.commonlib.R;
import com.example.a11829.commonlib.http.HttpTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zyf.fwms.commonlibrary.databinding.FragmentBaseBinding;
import com.zyf.fwms.commonlibrary.http.HttpUtils;
import com.zyf.fwms.commonlibrary.utils.AutoUtils;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.LogUtil;
import com.zyf.fwms.commonlibrary.utils.TUtil;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 刘宇飞创建 on 2017/5/18.
 * 描述：
 */
public abstract class BaseFragment<SV extends ViewDataBinding, T extends BasePresenter, V extends BaseView> extends Fragment {
    //布局view
    protected SV mBindingView;
    private CompositeSubscription mCompositeSubscription;
    // 内容布局
    protected RelativeLayout mContainer;
    protected Context mContext;
    //加载失败
    private LinearLayout mRefresh;
    protected HttpTask mHttpTask;
    public T mPresenter;
    private FragmentBaseBinding mBaseBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHttpTask = HttpUtils.getInstance().createRequest(HttpTask.class);
        mPresenter = TUtil.getT(this, 1);
        mContext = getContext();
        if (mPresenter != null) {
            mPresenter.httpTask = mHttpTask;
            mPresenter.mContext = mContext;
            mPresenter.setView((V)this);
        }
        //打印类名
        LogUtil.getInstance().e(getClass().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false);
        mBindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(), setContent(), null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBindingView.getRoot().setLayoutParams(params);
        mContainer = mBaseBinding.container;
        mContainer.addView(mBindingView.getRoot());
        mContext = getContext();
        Glide.get(getContext()).clearMemory();
        initLisener();
        initView();
        loadData();
        ButterKnife.bind(this, mBaseBinding.getRoot());
        AutoUtils.auto(mBaseBinding.getRoot());
        return mBaseBinding.getRoot();
    }

    protected abstract void initView();


    /**
     * 加载数据
     */
    protected void loadData() {
        LogUtil.getInstance().e("loadData()");
    }



    private void initLisener() {
        mBaseBinding.llErrorRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

    }







    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }

    /**
     * 布局
     */
    public abstract int setContent();

    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }


    /**
     * 显示toast
     *
     * @param title
     */
    protected void showToast(String title) {
        CommonUtils.showToast(mContext, title);
    }

    private KProgressHUD mProgressDialog;

    /**
     * 显示进度框
     *
     * @param str
     */
    public final void showInfoProgressDialog(final String... str) {
        if (mProgressDialog == null) {
            mProgressDialog = new KProgressHUD(mContext);
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
    protected void showErroView(boolean isShow) {
        if (isShow) {
            mRefresh.setVisibility(View.VISIBLE);
            mBindingView.getRoot().setVisibility(View.GONE);
        } else {
            mRefresh.setVisibility(View.GONE);
            mBindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 添加网络请求观察者
     *
     * @param s
     */
    public void addSubscription(Subscription s) {
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
            this.mCompositeSubscription=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideInfoProgressDialog();
        removeSubscription();
        ButterKnife.unbind(this);
        Glide.get(getContext()).clearMemory();
    }
}
