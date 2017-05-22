package com.zyf.fwms.commonlibrary.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 杭州融科网络
 * 刘宇飞 创建 on 2017/5/15.
 * 描述：
 */

public class HttpUtils {
    private Gson gson;
    private static volatile HttpUtils instance;
    private volatile Object httpTask;
    private static HashMap<String, Object> customHttpMaps = new HashMap<>();
    /**
     * 单例
     * @return
     */
    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    private Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOkClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    private OkHttpClient getOkClient() {
        //使用OkHttp拦截器可以指定需要的header给每一个Http请求
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())//日志
//            .addInterceptor(new NotEdcodeLoggingInterceptor())//不加密
             //   .addNetworkInterceptor(new RequestHeaderInterceptor())//请求头
                .build();
        return client;
    }

    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    /**
     * 一般请求
     * @param a
     * @param <T>
     * @return
     */
    public <T> T createRequest(Class<T> a) {
        if (httpTask == null) {
            synchronized (HttpUtils.class) {
                if (httpTask == null) {
                    httpTask = getBuilder(Api.HOST_URL).build().create(a);
                }
            }
        }
        return (T) httpTask;
    }


    /**
     * 一般请求
     * @param a
     * @param <T>
     * @return
     */
    public <T> T createRequest(Class<T> a,String baseUrl) {
        if(!customHttpMaps.containsKey(baseUrl)){
            customHttpMaps.put(baseUrl,getBuilder(baseUrl).build().create(a));
        }
        return (T) customHttpMaps.get(baseUrl);
    }

    /**
     * 清空重建 防止切换账号 使用共同的请求
     */
    public void clearHttp(){
        instance=null;
        httpTask=null;
        customHttpMaps.clear();
        gson=null;
    }
}
