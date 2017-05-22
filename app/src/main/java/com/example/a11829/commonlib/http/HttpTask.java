package com.example.a11829.commonlib.http;

import com.zyf.fwms.commonlibrary.http.Api;
import com.zyf.fwms.commonlibrary.http.BaseRespose;
import com.zyf.fwms.commonlibrary.model.UserInfoModel;

import java.util.Map;

import retrofit2.http.GET;
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
    @GET(Api.LOGINSMS)
    Observable<BaseRespose<UserInfoModel>> requestLogin(@QueryMap Map<String, Object> pram);
}
