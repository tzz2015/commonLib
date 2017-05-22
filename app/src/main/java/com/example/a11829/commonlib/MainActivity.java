package com.example.a11829.commonlib;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.a11829.commonlib.base.BasePresenter;
import com.example.a11829.commonlib.databinding.ActivityMainBinding;
import com.example.xrecyclerview.XRecyclerView;
import com.example.a11829.commonlib.base.BaseActivity;
import com.zyf.fwms.commonlibrary.base.baseadapter.OnItemClickListener;
import com.zyf.fwms.commonlibrary.base.baseadapter.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<BasePresenter,ActivityMainBinding> implements XRecyclerView.LoadingListener {

    private List<String> dataList=new ArrayList<>();
    private TestAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("RecyclerView示例");
        initData(false);
        initRecyclerView();
    }

    @Override
    protected void initPresenter() {

    }

    /**
     * 初始化recyclerview
     */
    private void initRecyclerView() {
        bindingView.xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 需加，不然滑动不流畅
        bindingView.xRecyclerView.setNestedScrollingEnabled(false);
        bindingView.xRecyclerView.setHasFixedSize(false);
        bindingView.xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TestAdapter();
        adapter.addAll(dataList);
        bindingView.xRecyclerView.setLoadingListener(this);
        bindingView.xRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(View view, String s, int position) {
                Toast.makeText(getApplicationContext(),"点击："+s,Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener<String>() {
            @Override
            public void onLongClick(View view, String s, int position) {
                Toast.makeText(getApplicationContext(),"长按："+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(boolean isMore) {
        if(!isMore){
           dataList.clear();
        }
        for(int i=0;i<20;i++){
            dataList.add("数据："+i);
        }
        bindingView.xRecyclerView.refreshComplete();
        if(adapter!=null){
            adapter.clear();
            adapter.addAll(dataList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        initData(false);
    }

    @Override
    public void onLoadMore() {
      initData(true);
    }
}
