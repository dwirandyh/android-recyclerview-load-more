package com.dwirandyh.android_recyclerview_load_more.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dwirandyh.android_recyclerview_load_more.R;
import com.dwirandyh.android_recyclerview_load_more.adapter.ItemAdapter;
import com.dwirandyh.android_recyclerview_load_more.adapter.OnLoadMoreListener;
import com.dwirandyh.android_recyclerview_load_more.data.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ItemAdapter adapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        adapter = new ItemAdapter(getData(page), recyclerView);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.addLoadingItem();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Object> nextItem = getData(page + 1);
                        adapter.removeLoadingItem();
                        for (Object obj : nextItem) {
                            adapter.addItem(obj);
                        }
                        adapter.setLoaded();
                        page++;
                    }
                }, 2000);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private List<Object> getData(int page) {
        List<Object> itemList = new ArrayList<>();
        int from = (page <= 1) ? 0 : 20 * (page - 1);
        int to = (page == 1) ? 20 : page * 20;
        for (int i = from; i <= to; i++) {
            User item = new User();
            item.setUserId(i);
            item.setName("Name " + i);
            itemList.add(item);
        }
        return itemList;
    }

}
