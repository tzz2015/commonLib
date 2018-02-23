package com.example.a11829.commonlib;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.a11829.commonlib.base.BaseActivity;
import com.example.a11829.commonlib.base.BasePresenter;
import com.example.a11829.commonlib.contact.DemoActivityContact;
import com.example.a11829.commonlib.databinding.ActivityMainBinding;
import com.example.a11829.commonlib.model.TestModel;
import com.example.xrecyclerview.XRecyclerView;
import com.zyf.fwms.commonlibrary.base.baseadapter.BaseRecyclerModel;
import com.zyf.fwms.commonlibrary.base.baseadapter.OnItemClickListener;
import com.zyf.fwms.commonlibrary.base.baseadapter.OnItemLongClickListener;
import com.zyf.fwms.commonlibrary.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<BasePresenter, ActivityMainBinding,DemoActivityContact.View> implements XRecyclerView.LoadingListener {


    private List<BaseRecyclerModel> dataList = new ArrayList<>();
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
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    /**
     * 初始化recyclerview
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBindingView.xRecyclerView.setLayoutManager(linearLayoutManager);
        // 需加，不然滑动不流畅
        mBindingView.xRecyclerView.setNestedScrollingEnabled(false);
        mBindingView.xRecyclerView.setHasFixedSize(false);
        mBindingView.xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new TestAdapter();
        adapter.addAll(dataList);
        mBindingView.xRecyclerView.setLoadingListener(this);
        mBindingView.xRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new OnItemClickListener<BaseRecyclerModel>() {
            @Override
            public void onClick(View view, BaseRecyclerModel s, int position) {
                if (s instanceof TestModel) {
                    showToast("点击：" + ((TestModel) s).name);
                }

            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener<BaseRecyclerModel>() {
            @Override
            public void onLongClick(View view, BaseRecyclerModel s, int position) {
                if (s instanceof TestModel) {
                    showToast("长按：" + ((TestModel) s).name);
                }

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(boolean isMore) {
        if (!isMore) {
            dataList.clear();
        }

        BaseRecyclerModel model=new BaseRecyclerModel();
        model.viewType=3;
        dataList.add(model);
        for(int i=0;i<20;i++){
            TestModel testModel=new TestModel();
            if(i%2==0){
                testModel.viewType=TestAdapter.TEST2_ITEM;//这里是关键 一一对应     类型2
                testModel.name="我是类型2--："+i;
            }else {
                testModel.viewType=TestAdapter.TEST_ITEM;//这里是关键 一一对应      类型1
                testModel.name="我是类型1--："+i;
            }


            dataList.add(testModel);
        }

        mBindingView.xRecyclerView.refreshComplete();
        if (adapter != null) {
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
