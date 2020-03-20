package com.example.slidingconflict.p3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.slidingconflict.R;
import com.example.slidingconflict.p1.NewsListFragment;
import com.example.slidingconflict.p1.NewsListFragment2;
import com.google.android.material.tabs.TabLayout;

public class Main3Activity extends AppCompatActivity {
    String[] titles = new String[]{"简介", "评论", "相关"};
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    StickTabLayout2 stickTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        viewPager = findViewById(R.id.viewPaper);
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                NewsListFragment newsListFragment = new NewsListFragment();
                Bundle args = new Bundle();
                args.putString(NewsListFragment.KEY_TITLE, titles[position]);
                newsListFragment.setArguments(args);
                return newsListFragment;
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        stickTabLayout =findViewById(R.id.sLayout);
        stickTabLayout.setScrollUpTarget(new StickTabLayout2.ScrollUpTarget() {
            @Override
            public boolean canScrollUp() {
                NewsListFragment newsListFragment = (NewsListFragment) fragmentPagerAdapter.instantiateItem(viewPager,viewPager.getCurrentItem());
                return !newsListFragment.isTop();
            }
        });
    }
}
