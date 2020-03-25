package com.example.slidingconflict.p5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.slidingconflict.R;

public class Main5Activity extends AppCompatActivity {

    private static final String TAG = Main5Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        MyListView myListView = findViewById(R.id.list_view);
        myListView.setAdapter(new MyListView.Adapter() {
            @Override
            public int getCount() {
                return 50;
            }

            @Override
            public View getView(MyListView myListView) {
                return LayoutInflater.from(myListView.getContext()).inflate(R.layout.layout_list_item, myListView, false);

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void updateView(View view, int position) {
                Log.i(TAG, "updateView view=" + view + " position=" + position);
                TextView textView = view.findViewById(R.id.tv_title);
                String s = position + "";
                if (position % 5 == 0) {
                    view.setBackgroundColor(Color.RED);
                    view.getLayoutParams().height=200;
                } else {
                    view.setBackgroundColor(Color.WHITE);
                    view.getLayoutParams().height=100;
                }
                textView.setText(s);
            }
        });
    }
}
