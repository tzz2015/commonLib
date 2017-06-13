package com.zyf.fwms.commonlibrary.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyf.fwms.commonlibrary.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by jingbin on 2016/11/26.
 */

public class ImgLoadUtil {

    private static ImgLoadUtil instance;

    private ImgLoadUtil() {}

    public static ImgLoadUtil getInstance() {
        if (instance == null) {
            instance = new ImgLoadUtil();
        }
        return instance;
    }






    public static void displayGif(String url, ImageView imageView) {

        Glide.with(imageView.getContext()).load(url)
                .asBitmap()
                .placeholder(R.mipmap.img_one_bi_one)
                .error(R.mipmap.img_one_bi_one)
//                .skipMemoryCache(true) //跳过内存缓存
//                .crossFade(1000)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)// 缓存图片源文件（解决加载gif内存溢出问题）
//                .into(new GlideDrawableImageViewTarget(imageView, 1));
                .into(imageView);
    }




    /**
     * 显示高斯模糊效果（电影详情页）
     */
    private static void displayGaussian(Context context, String url, ImageView imageView) {
        // "23":模糊度；"4":图片缩放4倍后再进行模糊
        Glide.with(context)
                .load(url)
                .error(R.mipmap.img_one_bi_one)
                .placeholder(R.mipmap.img_one_bi_one)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(context, 23, 4))
                .into(imageView);
    }

    /**
     * 加载圆角图,暂时用到显示头像
     */
    public static void displayCircle(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .crossFade(500)
                .error(R.mipmap.img_one_bi_one)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }


    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.img_one_bi_one)
                .error(R.mipmap.img_one_bi_one)
                .crossFade().into(imageView);
    }
    // 加载一般图片
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url){
        display(view.getContext(),view,url);
    }
    // 加载圆形图片
    @BindingAdapter({"circleImageUrl"})
    public static void loadCircleImage(ImageView view, String url){
        displayCircle(view,url);
    }
    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.img_one_bi_one)
                .error(R.mipmap.img_one_bi_one)
                .crossFade().into(imageView);
    }
    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.img_one_bi_one)
                .error(R.mipmap.img_one_bi_one)
                .thumbnail(0.5f)
                .into(imageView);
    }
    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.img_one_bi_one)
                .error(R.mipmap.img_one_bi_one)
                .into(imageView);
    }
    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.img_one_bi_one)
                .error(R.mipmap.img_one_bi_one)
                .crossFade().into(imageView);
    }
    public static void displayRound(Context context,ImageView imageView, String url,int... defaut_poto) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        int defautUrl=R.mipmap.img_one_bi_one;
        if(defaut_poto!=null&&defaut_poto.length==1){
            defautUrl=defaut_poto[0];
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(defautUrl)
                .centerCrop().transform(new GlideCircleTransform(context)).into(imageView);
    }
    public static void displayRound(Context context,ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.img_one_bi_one)
                .centerCrop().transform(new GlideCircleTransform(context)).into(imageView);
    }



}
