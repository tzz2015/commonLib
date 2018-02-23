package com.example.a11829.commonlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.example.a11829.commonlib.base.BaseActivity;
import com.example.a11829.commonlib.base.BasePresenter;
import com.example.a11829.commonlib.databinding.ActivityTencentX5Binding;
import com.example.xrecyclerview.CheckNetwork;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.LogUtil;
import com.zyf.fwms.commonlibrary.x5webview.X5WebView;
import com.zyf.fwms.commonlibrary.x5webview.X5WebViewJavaScriptFunction;

public class TencentX5Activity extends BaseActivity<BasePresenter,ActivityTencentX5Binding> {

    private X5WebView webView;
    private WebSettings settings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tencent_x5);

    }

    @Override
    protected void initView() {
        webView = new X5WebView(mContext.getApplicationContext());
        mBindingView.flContent.removeAllViews();
        mBindingView.flContent.addView(webView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        initSetting();
        webView.loadUrl("https://mall.imeihao.shop/");
        ((BaseActivity) mContext).showInfoProgressDialog();

    }

    @Override
    protected void initPresenter() {

    }


    private void initSetting() {


        if (webView == null) return;

        settings = webView.getSettings();
        String appCachePath = MyApplication.getContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);// 实现8倍缓存
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
        String userAgentString = settings.getUserAgentString();
        LogUtil.getInstance().e(userAgentString);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationDatabasePath(mContext.getDir("geolocation", 0).getPath());
        //支持缩放
        settings.setBuiltInZoomControls(true);
        if (CheckNetwork.isNetworkConnected(mContext))
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        else
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //禁止加载图片
        settings.setBlockNetworkImage(true);
        //提高渲染优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                if (mContext != null && mContext instanceof BaseActivity)
                    ((BaseActivity) mContext).hideInfoProgressDialog();
                settings.setBlockNetworkImage(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return super.shouldOverrideUrlLoading(webView, s);

            }
        });

        webView.addJavascriptInterface(new X5WebViewJavaScriptFunction() {
            @Override
            public void onJsFunctionCalled(String tag) {

            }


            @JavascriptInterface
            public void backFun() {
                ((Activity) mContext).finish();
            }

        }, "fgqqg");
    }
}
