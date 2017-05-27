package com.example.xrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangcai on 2016/1/28.
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_REFRESH_HEADER = -5;
    private static final int TYPE_HEADER = -4;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = -3;
    private RecyclerView.Adapter adapter;

    private SparseArray<View> mHeaderViews;

    private SparseArray<View> mFootViews;

    private int headerPosition = 1;

    public WrapAdapter(SparseArray<View> headerViews, SparseArray<View> footViews, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        this.mHeaderViews = headerViews;
        this.mFootViews = footViews;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeader(position) || isFooter(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /**
     * 是否是头布局
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return position >= 0 && position < mHeaderViews.size();
    }

    /**
     * 是否为底布局
     * @param position
     * @return
     */
    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - mFootViews.size();
    }

    /**
     * 是否可以刷新
     * @param position
     * @return
     */
    public boolean isRefreshHeader(int position) {
        return position == 0;
    }

    /**
     * 头布局数目
     * @return
     */
    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    /**
     * 底布局数目
     * @return
     */
    public int getFootersCount() {
        return mFootViews.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REFRESH_HEADER) {  //刷新头布局
            return new SimpleViewHolder(mHeaderViews.get(0));
        } else if (viewType == TYPE_HEADER) {  //自定义头布局
            return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
        } else if (viewType == TYPE_FOOTER) {  //底布局
            return new SimpleViewHolder(mFootViews.get(0));
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        int adjPosition = position - getHeadersCount();
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                adapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (adapter != null) {
            return getHeadersCount() + getFootersCount() + adapter.getItemCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isRefreshHeader(position)) {//刷新头布局
            return TYPE_REFRESH_HEADER;
        }
        if (isHeader(position)) {//头布局
            return TYPE_HEADER;
        }
        if (isFooter(position)) {//底布局
            return TYPE_FOOTER;
        }
        int adjPosition = position - getHeadersCount();
        int adapterCount;
        if (adapter != null) {//适配器布局
            adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemViewType(adjPosition);
            }
        }
        return TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        if (adapter != null && position >= getHeadersCount()) {
            int adjPosition = position - getHeadersCount();
            int adapterCount = adapter.getItemCount();
            if (adjPosition < adapterCount) {
                return adapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}