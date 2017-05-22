package com.example.a11829.commonlib;

import android.view.ViewGroup;

import com.example.a11829.commonlib.databinding.LayoutTestItemBinding;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewHolder;

/**
 *
 * 刘宇飞创建 on 2017/5/18.
 * 描述：
 */
public class TestHolder extends BaseRecyclerViewHolder<String,LayoutTestItemBinding> {
    public TestHolder(ViewGroup parent, int title) {
        super(parent,title);
    }

    @Override
    public void onBindViewHolder(String object, int position) {
        if(object!=null){
            binding.setItem(object);
        }
    }
}
