package com.example.slidingconflict.p6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.slidingconflict.R;
import com.example.slidingconflict.p1.SimpleAdapter;

public class Main6Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new SimpleAdapter("新闻"));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setScrollContent(new RefreshLayout.ScrollContent() {


            @Override
            public boolean canScrollUp() {

                return linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0;
            }

            @Override
            public boolean canScrollDown() {
                int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int lastIndex = linearLayoutManager.getItemCount() - 1;
                return lastCompletelyVisibleItemPosition != lastIndex;
            }
        });
        refreshLayout.setRefreshListener(new RefreshLayout.RefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(()->refreshLayout.setRefresh(false),3000);
            }
        });
    }
}
