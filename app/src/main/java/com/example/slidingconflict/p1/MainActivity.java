package com.example.slidingconflict.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.slidingconflict.R;

public class MainActivity extends AppCompatActivity {
    MyLayout myLayout;
    ViewPager viewPager;
    String[] titles = new String[]{"头条", "热点", "本地", "经济", "军事"};
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout=findViewById(R.id.myLayout);
        viewPager = findViewById(R.id.viewPaper);
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                NewsListFragment2 newsListFragment = new NewsListFragment2();
                Bundle args = new Bundle();
                args.putString(NewsListFragment.KEY_TITLE, titles[position]);
                newsListFragment.setArguments(args);
                return newsListFragment;
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        myLayout.setIsTopListener(new MyLayout.IsTopListener() {
            @Override
            public boolean isTop() {
                NewsListFragment2 newsListFragment = (NewsListFragment2) fragmentPagerAdapter.instantiateItem(viewPager,
                        viewPager.getCurrentItem());

                return newsListFragment.isTop();
            }
        });

    }
}
