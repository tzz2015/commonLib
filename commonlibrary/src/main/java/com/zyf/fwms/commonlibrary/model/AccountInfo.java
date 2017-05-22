package com.zyf.fwms.commonlibrary.model;

import java.io.Serializable;

/**
 *
 * 刘宇飞 创建 on 2017/3/5.
 * 描述：用户信息
 */

public class AccountInfo implements Serializable {
    public static long id=222;

    /**
     * 清空用户信息
     */
    public static void clearInfo() {
        id = 0;

    }


}
