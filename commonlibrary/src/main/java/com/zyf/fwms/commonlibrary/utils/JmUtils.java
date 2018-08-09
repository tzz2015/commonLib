package com.zyf.fwms.commonlibrary.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 创建 by lyf on 2018/7/10.
 * 描述：解密
 */

public class JmUtils {
    private static String a = "aaaabbbbccccdddd";

    public static String jm(String paramString)
    {
        try
        {
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(a.getBytes(), "AES");
            Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            localCipher.init(2, localSecretKeySpec);
            paramString = localCipher.doFinal(Base64.decode(paramString, 2)).toString();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            paramString = null;
        }
        return new String(paramString);
    }
}
