package com.example.a11829.commonlib.http;

import com.example.a11829.commonlib.model.AccountInputModel;
import com.google.gson.JsonObject;
import com.zyf.fwms.commonlibrary.http.Api;
import com.zyf.fwms.commonlibrary.http.BaseRespose;
import com.zyf.fwms.commonlibrary.model.AccountInfo;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 *
 * 刘宇飞 创建 on 2017/5/20.
 * 描述：
 */

public interface HttpTask {
    /**
     * 登录
     **/
    @GET(Api.LIVE_LIST)
    Observable<JsonObject> requestLogin();
}
