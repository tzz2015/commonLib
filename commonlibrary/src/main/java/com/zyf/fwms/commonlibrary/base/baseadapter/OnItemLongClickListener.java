package com.zyf.fwms.commonlibrary.base.baseadapter;

import android.view.View;

/**
 * Created by jingbin on 16/7/4.
 */
public interface OnItemLongClickListener<T> {
    public void onLongClick(View view, T t, int position);
}
