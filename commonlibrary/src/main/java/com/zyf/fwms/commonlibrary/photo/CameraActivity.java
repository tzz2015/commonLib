package com.zyf.fwms.commonlibrary.photo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.zyf.fwms.commonlibrary.R;
import com.zyf.fwms.commonlibrary.utils.AutoUtils;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;
import com.zyf.fwms.commonlibrary.utils.StatusBarUtil;

import java.io.File;

public class CameraActivity extends TakePhotoActivity {
    /**
     * 是否裁剪 默认不裁剪
     */
    private boolean isCrop;
    /**
     * 最大相片数
     */
    private int maxSize;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        AutoUtils.auto(this);
        // 设置透明状态栏
        StatusBarUtil.setColor(this, CommonUtils.getColor(this, R.color.colorTitle), 0);
        initView();
    }

    private void initView() {
        maxSize = getIntent().getIntExtra("maxSize", 1);
        isCrop = getIntent().getBooleanExtra("isCrop", false);
        initOption();
        //拍摄
        findViewById(R.id.tv_take_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCrop) {
                    getTakePhoto().onPickFromCaptureWithCrop(imageUri, getCropOptions());
                } else {
                    getTakePhoto().onPickFromCapture(imageUri);
                }
                // finish();
            }
        });
        //相册中选取
        findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maxSize > 1) {
                    if (isCrop) {
                        getTakePhoto().onPickMultipleWithCrop(maxSize, getCropOptions());
                    } else {
                        getTakePhoto().onPickMultiple(maxSize);
                    }
                    return;
                }

                if(isCrop){
                    getTakePhoto().onPickFromDocumentsWithCrop(imageUri,getCropOptions());
                }else {
                    getTakePhoto().onPickFromDocuments();
                }

            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化参数
     */
    private void initOption() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//使用TakePhoto自带相册
        builder.setCorrectImage(true);//纠正拍照的照片旋转角度
        getTakePhoto().setTakePhotoOptions(builder.create());
        //压缩设置
        LubanOptions option = new LubanOptions.Builder()
                .setMaxHeight(800)
                .setMaxWidth(480)
                .setMaxSize(maxSize)
                .create();
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);//是否保存原图
        getTakePhoto().onEnableCompress(config, true);
    }

    /**
     * 裁剪参数设置
     */
    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(800).setAspectY(800);
        builder.setWithOwnCrop(true);
        return builder.create();
    }


    @Override
    public void takeCancel() {
        super.takeCancel();
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        finish();

    }

    @Override
    public void takeSuccess(final TResult result) {
        super.takeSuccess(result);

        if (PhotoModel.callback != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PhotoModel.callback.onHanlderSuccess(result.getImages());
                }
            });

        }
        finish();
    }


}
