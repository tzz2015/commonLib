package com.example.a11829.commonlib;

import android.view.ViewGroup;

import com.example.a11829.commonlib.databinding.LayoutTestItemBinding;
import com.example.a11829.commonlib.model.TestModel;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerModel;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewHolder;

/**
 *
 * 刘宇飞创建 on 2017/5/18.
 * 描述：
 */
public class TestHolder extends BaseRecyclerViewHolder<LayoutTestItemBinding> {
    public TestHolder(ViewGroup parent, int title) {
        super(parent,title);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerModel model, int position) {
        if(model instanceof TestModel){
            binding.setItem((TestModel) model);
        }

    }
}
