package com.zyf.fwms.commonlibrary.base.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zyf.fwms.commonlibrary.utils.PerfectClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jingbin on 2016/11/25
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected List<BaseRecyclerModel> data = new ArrayList<>();
    protected OnItemClickListener<BaseRecyclerModel> listener;
    protected OnItemLongClickListener<BaseRecyclerModel> onItemLongClickListener;

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        holder.onBaseBindViewHolder(data.get(position), position);
        holder.itemView.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (listener!=null){
                    listener.onClick(v,data.get(position), position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener!=null){
                    onItemLongClickListener.onLongClick(v,data.get(position), position);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position) instanceof BaseRecyclerModel){
            if(( data.get(position)).viewType!=0){
                return (data.get(position)).viewType;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<BaseRecyclerModel> data) {
        this.data.addAll(data);
    }

    public void add(BaseRecyclerModel object) {
        data.add(object);
    }

    public void clear() {
        data.clear();
    }

    public void remove(BaseRecyclerModel object) {
        data.remove(object);
    }
    public void remove(int position) {
        data.remove(position);
    }
    public void removeAll(List<BaseRecyclerModel> data) {
        this.data.retainAll(data);
    }

    public void setOnItemClickListener(OnItemClickListener<BaseRecyclerModel> listener) {
        this.listener = listener;
    }


    public List<BaseRecyclerModel> getData() {
        return data;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<BaseRecyclerModel> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
