package com.example.a11829.commonlib;

import android.os.Bundle;

import com.example.a11829.commonlib.base.BaseActivity;
import com.example.a11829.commonlib.base.BasePresenter;
import com.example.a11829.commonlib.databinding.ActivityNotitleBinding;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.StatusBarUtil;
import com.zyf.fwms.commonlibrary.utils.statusbar.StatusBarFontHelper;

public class NotitleActivity extends BaseActivity<BasePresenter,ActivityNotitleBinding>  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notitle);
        StatusBarUtil.setColor(this, CommonUtils.getColor(this, com.zyf.fwms.commonlibrary.R.color.colorWhite),0);
        //设置标题栏字体为黑色
        StatusBarFontHelper.setStatusBarMode(this, true);
        hideTitleBar();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenter() {

    }
}
