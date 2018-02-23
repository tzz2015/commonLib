package com.example.a11829.commonlib;

import android.view.ViewGroup;


import com.example.a11829.commonlib.databinding.ItemSysMsgBinding;
import com.example.a11829.commonlib.widget.AutoScrollTextView;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerModel;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerViewHolder;
import com.zyf.fwms.commonlibrary.utils.AutoUtils;
import com.zyf.fwms.commonlibrary.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 杭州融科网络
 * 刘宇飞创建 on 2017/6/6.
 * 描述：
 */

public class MainHomeSysMsgHolder extends BaseRecyclerViewHolder<ItemSysMsgBinding> {
    private List<String> list = new ArrayList<>();

    public MainHomeSysMsgHolder(ViewGroup viewGroup, int layoutId) {
        super(viewGroup, layoutId);
        list.add("不能适配吗！！！！");
        list.add("开玩笑吧！！！！");
        list.add("真的！！！！");
        list.add("那再试试吧！！！！");
        list.add("真不行！！！！");
        list.add("不能说不行！！！！");
        list.add("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊！！！！");
    }

    @Override
    public void onBindViewHolder(BaseRecyclerModel object, int position) {

        if(binding.llRoot.getChildCount()==2) binding.llRoot.removeViewAt(1);
        // binding.autoText.setUserTextView(R.layout.scroll_textview_item);
        AutoScrollTextView autoText = new AutoScrollTextView(itemView.getContext());
        autoText.setPadding(0,15,0,0);
        AutoUtils.autoPadding(autoText);
        autoText.setList(list);
        autoText.stopScroll();
        autoText.startScroll();
        autoText.setClickLisener(new AutoScrollTextView.ItemClickLisener() {
            @Override
            public void onClick(int position) {
                CommonUtils.showToast(itemView.getContext(), list.get(position));
            }
        });
        binding.llRoot.addView(autoText);


    }
}

