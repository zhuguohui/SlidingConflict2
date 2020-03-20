package com.example.slidingconflict.p1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slidingconflict.R;

public class NewsListFragment2 extends Fragment {

    public static final String KEY_TITLE = "key_title";
    private String title;
    private LinearLayout layout;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(KEY_TITLE, "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = (LinearLayout) view.findViewById(R.id.layout);
        int childCount = layout.getChildCount();
        for (int i = 0; i <childCount ; i++) {
            RelativeLayout relativeLayout = (RelativeLayout) layout.getChildAt(i);
            TextView textView = (TextView) relativeLayout.getChildAt(0);
            textView.setText(title + i);

        }

    }

    public boolean isTop() {
        return layout.getScrollY()==0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
