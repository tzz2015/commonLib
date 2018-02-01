package com.zyf.fwms.commonlibrary.http;

import java.io.Serializable;

/**
 * 公司：
 * 刘宇飞 创建  2017/3/6.
 * 描述：
 */

public class BaseRespose<T> implements Serializable {



    public int code;
    public T data;
    public String msg;
    public boolean success;


}