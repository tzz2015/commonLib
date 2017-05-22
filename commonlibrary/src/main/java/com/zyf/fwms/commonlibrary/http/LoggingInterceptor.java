/*
 * Mr.Mantou - On the importance of taste
 * Copyright (C) 2015  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.zyf.fwms.commonlibrary.http;


import com.zyf.fwms.commonlibrary.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        printRequest(request);


        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();
        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
        try {
            String logResult = new String(responseBodyString.toCharArray());
            int indexOf = request.url().toString().lastIndexOf("/");
            LogUtil.getInstance().e("接口:" + request.url().toString().substring(indexOf+1,request.url().toString().length()) + "--返回数据:" + logResult);
        }catch (Exception e){

        }
        return newResponse;

    }

    /**
     * 打印请求体
     * @param request
     */
    private void printRequest(Request request) {
        try {
            String result = new String(bodyToString(request).toCharArray());
            LogUtil.getInstance().e("请求url:" + request.url() + "?" + result);

        } catch (Exception e) {
            LogUtil.getInstance().e(e);
        }
    }



    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if(copy!=null&&copy.body()!=null){
                copy.body().writeTo(buffer);
            }else {
                return "";
            }

            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }

}
