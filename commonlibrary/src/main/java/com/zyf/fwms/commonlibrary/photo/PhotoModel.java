package com.zyf.fwms.commonlibrary.photo;

import android.content.Context;
import android.content.Intent;

import com.jph.takephoto.model.TImage;

import java.util.List;

/**
 * 杭州融科网络
 * 刘宇飞创建 on 2017/6/13.
 * 描述：拍照工具类
 */

public class PhotoModel {
    private Context context;

    /**
     * 是否裁剪 默认不裁剪
     */
   public   boolean isCrop;
    /**
     * 最大相片数
     */
    public   int maxSize;
    /**
     * 回调
     */
    public static OnHanlderResultCallback callback;

    public PhotoModel(Context context) {
        this.context = context;
    }
    public void setCallback(OnHanlderResultCallback callback){
        this.callback=callback;
    }


    /**
     * 初始化
     */
    public  void initTakePhoto(){
       if(maxSize==0) maxSize=1;
        Intent intent=new Intent(context,CameraActivity.class);
        intent.putExtra("isCrop",isCrop);
        intent.putExtra("maxSize",maxSize);
        context.startActivity(intent);
    }

    /**
     * 处理结果
     */
    public static interface OnHanlderResultCallback {
        /**
         * 处理成功
         * @param resultList
         */
        public void onHanlderSuccess(List<TImage> resultList);


    }


}
