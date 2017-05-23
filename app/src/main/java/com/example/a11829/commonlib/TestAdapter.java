package com.example.a11829.commonlib;

import android.view.ViewGroup;

import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewAdapter;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewHolder;

/**
 *
 * 刘宇飞创建 on 2017/5/18.
 * 描述：
 */

public class TestAdapter extends BaseRecyclerViewAdapter<String> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestHolder(parent, R.layout.layout_test_item);
    }


}
