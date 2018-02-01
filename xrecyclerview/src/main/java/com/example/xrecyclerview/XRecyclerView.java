package com.example.xrecyclerview;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

/**
 * Created by jingbin on 2016/1/28.
 */
public class XRecyclerView extends RecyclerView {
    private LoadingListener mLoadingListener;
    private WrapAdapter mWrapAdapter;
    private SparseArray<View> mHeaderViews = new SparseArray<View>();
    private SparseArray<View> mFootViews = new SparseArray<View>();
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private YunRefreshHeader mRefreshHeader;
    private boolean isLoadingData;
    public int previousTotal;
    public boolean isnomore;
    private float mLastY = -1;
    private static final float DRAG_RATE = 1.75f;
    // 是否是额外添加FooterView
    private boolean isOther = false;
    private boolean sIsScrolling;//是否在滚动
    //纵坐标滚动的距离
    private int dy = 0;
    private UpDownListener upDownListener;
    private boolean isStopLoadImg=true;


    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (pullRefreshEnabled) {
            YunRefreshHeader refreshHeader = new YunRefreshHeader(context);
            mHeaderViews.put(0, refreshHeader);
            mRefreshHeader = refreshHeader;
        }
        LoadingMoreFooter footView = new LoadingMoreFooter(context);
        addFootView(footView, false);
        mFootViews.get(0).setVisibility(GONE);
    }

    /**
     * 改为公有。供外添加view使用,使用标识
     * 注意：使用后不能使用 上拉加载，否则添加无效
     * 使用时 isOther 传入 true，然后调用 noMoreLoading即可。
     */
    public void addFootView(final View view, boolean isOther) {
        mFootViews.clear();
        mFootViews.put(0, view);
        this.isOther = isOther;
    }

    /**
     * 相当于加一个空白头布局：
     * 只有一个目的：为了滚动条显示在最顶端
     * 因为默认加了刷新头布局，不处理滚动条会下移。
     * 和 setPullRefreshEnabled(false) 一块儿使用
     * 使用下拉头时，此方法不应被使用！
     */
    public void clearHeader() {
        mHeaderViews.clear();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int height = (int) (1.0f * scale + 0.5f);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        View view = new View(getContext());
        view.setLayoutParams(params);
        mHeaderViews.put(0, view);
    }

    public void addHeaderView(View view) {
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof YunRefreshHeader)) {
            YunRefreshHeader refreshHeader = new YunRefreshHeader(getContext());
            mHeaderViews.put(0, refreshHeader);
            mRefreshHeader = refreshHeader;
        }
        mHeaderViews.put(mHeaderViews.size(), view);
    }

    private void loadMoreComplete() {
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (previousTotal <= getLayoutManager().getItemCount()) {
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_COMPLETE);
            } else {
                footView.setVisibility(View.GONE);
            }
        } else {
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
            } else {
                footView.setVisibility(View.GONE);
            }
            isnomore = true;
        }
        previousTotal = getLayoutManager().getItemCount();
    }

    public void noMoreLoading() {
        isLoadingData = false;
        final View footView = mFootViews.get(0);
        isnomore = true;
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
        // 额外添加的footView
        if (isOther) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    public void refreshComplete() {
        //  mRefreshHeader.refreshComplate();
        if (isLoadingData) {
            loadMoreComplete();
        } else {
            mRefreshHeader.refreshComplate();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);

        // 调用 notifyDataSetChanged 导致界面闪烁问题
        RecyclerView.ItemAnimator animator = this.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //滚动是否停止 是否有监听 是否需要加载更多 没有正在加载数据
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;   //最后可见索引
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isnomore && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING) {

                View footView = mFootViews.get(0);
                isLoadingData = true;
                if (footView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    footView.setVisibility(View.VISIBLE);
                }
                if (CheckNetwork.isNetworkConnected(getContext())) {
                    mLoadingListener.onLoadMore();
                } else {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingListener.onLoadMore();
                        }
                    }, 1000);
                }
            }
        }

        if (state == RecyclerView.SCROLL_STATE_DRAGGING || state == RecyclerView.SCROLL_STATE_SETTLING) {//滚动中和惯性滑动
            sIsScrolling = true;
         if(isStopLoadImg)
             Glide.with(getContext()).pauseRequests();
        } else if (state == RecyclerView.SCROLL_STATE_IDLE) {//停止滚动
            if (sIsScrolling == true) {
                try {//停止滚动 清空图片内存
                    Glide.get(getContext()).clearMemory();
                    Glide.with(getContext()).resumeRequests();
                }catch (Exception e){

                }

            }
            sIsScrolling = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                //当手指按下的时候
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisiableHeight() > 0 && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                //手指上抬
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    if (dy > 0) {
                        // Log.e("XXXX:", "向上滑");
                        if (upDownListener != null) upDownListener.onUpScorll();
                    } else {
                        // Log.e("XXXX:", "向下滑");
                        if (upDownListener != null) upDownListener.onDownScorll();
                    }
                }

                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                            isnomore = false;
                            previousTotal = 0;
                            final View footView = mFootViews.get(0);
                            if (footView instanceof LoadingMoreFooter) {
                                if (footView.getVisibility() != View.GONE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    public boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.size() == 0) {
            return false;
        }

        View view = mHeaderViews.get(0);
        if (view.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };


    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        this.pullRefreshEnabled = pullRefreshEnabled;
    }

    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.loadingMoreEnabled = loadingMoreEnabled;
        if (!loadingMoreEnabled) {
            if (mFootViews != null) {
                mFootViews.remove(0);
            }
        } else {
            if (mFootViews != null) {
                LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
                addFootView(footView, false);
            }
        }
    }


    public void setLoadMoreGone() {
        if (mFootViews == null) {
            return;
        }
        View footView = mFootViews.get(0);
        if (footView != null && footView instanceof LoadingMoreFooter) {
            mFootViews.remove(0);
        }
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    /**
     * 上下滑动监听
     */
    public interface UpDownListener {

        void onUpScorll();

        void onDownScorll();
    }

    /**
     * 设置上下滑动监听
     */
    public void setUpDownListener(UpDownListener listener) {
        this.upDownListener = listener;
    }

    /**
     * 重新设置列数
     */
    public void setSpanCount(int num) {
        RecyclerView.LayoutManager manager = this.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanCount(num);
        }
    }

    public void reset() {
        isnomore = false;
        previousTotal = 0;
        final View footView = mFootViews.get(0);
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).reSet();
        }
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        this.dy = dy;

    }
    /**
     * 是否滚动中禁止加载
     */
    public void setStopLoadImg(boolean b){
        isStopLoadImg=b;
    }
}
