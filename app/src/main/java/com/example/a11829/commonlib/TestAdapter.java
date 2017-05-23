package com.example.a11829.commonlib;

import android.view.ViewGroup;

import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewAdapter;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewHolder;

/**
 *
 * 刘宇飞创建 on 2017/5/18.
 * 描述：
 */

public class TestAdapter extends BaseRecyclerViewAdapter {
    //各种类型
    public static final int TEST_ITEM=1;
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case TEST_ITEM:
                    return new TestHolder(parent, R.layout.layout_test_item);
                default://这里执行到default里面 一定会崩溃
                    return null;
            }

    }


}
