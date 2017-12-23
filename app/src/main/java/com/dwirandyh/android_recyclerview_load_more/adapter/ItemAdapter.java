package com.dwirandyh.android_recyclerview_load_more.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dwirandyh.android_recyclerview_load_more.R;
import com.dwirandyh.android_recyclerview_load_more.data.User;


import java.util.List;

/**
 * Created by Dwi Randy Herdinanto on 5/10/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE = 0;
    private static final int ITEM_LOADING = 2;

    public List<Object> items;
    public RecyclerView recyclerView;

    private boolean isLoading = false;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public ItemAdapter(List<Object> items, RecyclerView recyclerView) {
        this.items = items;
        this.recyclerView = recyclerView;

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager lim = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = lim.getItemCount();
                lastVisibleItem = lim.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_LOADING:
                View loadingView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(loadingView);
            case ITEM_VIEW_TYPE:
            default:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user, parent, false);
                return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            User item = (User) items.get(position);
            itemHolder.userId.setText(String.valueOf(item.getUserId()));
            itemHolder.name.setText(item.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return ITEM_LOADING;
        }
        return ITEM_VIEW_TYPE;
    }

    public void addItem(java.lang.Object item) {
        this.items.add(item);
        notifyItemInserted(this.items.size() - 1);
    }


    public void addLoadingItem() {
        removeLoadingItem();
        addItem(null);
    }

    public void removeLoadingItem() {
        try {
            Object item = this.items.get(this.items.size() - 1);
            if (item == null) {
                this.items.remove(this.items.size() - 1);
                notifyItemRemoved(this.items.size());
            }
        } catch (Exception e) {
            Log.d("ItemAdpater", e.getMessage());
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView userId;
        public TextView name;

        public ItemHolder(final View itemView) {
            super(itemView);

            userId = itemView.findViewById(R.id.userId);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }


    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}