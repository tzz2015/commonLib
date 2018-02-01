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
    public static final int TEST2_ITEM=2;
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case TEST_ITEM://文字在左边
                    return new TestHolder(parent, R.layout.layout_test_item);
                case TEST2_ITEM://文字在右边
                    return new TestHolder2(parent, R.layout.layout_test_item2);
                case 3:
                    return  new MainHomeSysMsgHolder(parent,R.layout.item_sys_msg);
                default://这里执行到default里面 一定会崩溃
                    return null;
            }

    }


}
