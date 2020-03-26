package com.example.slidingconflict.p6;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.example.slidingconflict.R;

public class RefreshLayout extends LinearLayout {

    private static final String TAG = RefreshLayout.class.getSimpleName();
    private int headerHeight;
    private int footerHeight;
    private int offsetY;
    private int scrollMax;
    private State state = State.state_idle;
    private ProgressBar progressBar;
    private TextView tvRefresh;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RefreshLayout(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScrollingEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        getChildAt(1).measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        headerHeight = getChildAt(0).getMeasuredHeight();
        footerHeight = getChildAt(2).getMeasuredHeight();
        setMeasuredDimension(width, headerHeight + height + footerHeight);
        scrollMax = headerHeight + footerHeight;
        offsetY = headerHeight;
        progressBar = findViewById(R.id.progressBar);
        tvRefresh = findViewById(R.id.tv_refresh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(state!=State.state_idle){
            return;
        }
        super.onLayout(changed, l, t, r, b);
        scrollTo(0, offsetY);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if ((nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // super.onNestedPreScroll(target, dx, dy, consumed);
        Log.i(TAG, "onNestedPreScroll scrollY=" + getScrollY());
        //向下滚动
        final int scrollY = getScrollY();
        if (state != State.state_idle) {
            consumed[1] = dy;
            //正在处理，屏蔽所有触摸事件
            return;
        }
        if (dy < 0) {

            if (scrollContent != null && !scrollContent.canScrollUp() && scrollY > 0) {
                consumed[1] = -Math.min(Math.abs(dy), scrollY);
                scrollBy(0, consumed[1] / 3);

            }
        } else {
            //向上滚动
            if (scrollContent != null && !scrollContent.canScrollDown() && scrollY < scrollMax) {
                consumed[1] = Math.min(dy, scrollMax - scrollY);
                scrollBy(0, consumed[1] / 3);

            }

        }
    }

    boolean accepted = false;

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.i(TAG, "onNestedScrollAccepted scrollY=" + getScrollY());
        super.onNestedScrollAccepted(child, target, axes);
        accepted = true;
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    /*    if(accepted){
            accepted=false;
            return;
        }*/
        changeStateByScrollY();
        Log.i(TAG, "onStopNestedScroll scrollY=" + getScrollY());
    }

    private void changeStateByScrollY() {
        final int scrollY = getScrollY();
        //headerHeight到0.5*headerHeight 为自动隐藏header scrollY 终点值为headHeight state 为hide_header
        //0到 0.5*headerHeight 位自动展开header 并开始刷新，scrollY 终点值为0 state 为loading
        //headHeight 到 headHeight+0.5*footerHeight 为自动隐藏footer scrollY终点值为headerHeight state 为hide_footer;
        //headerHeight+0.5footerHeight 以上 为开始加载更多 终点值为maxScrollY state 为load_more
        int endY = 0;
        State endState = State.state_idle;
        if (scrollY <= 0.25 * headerHeight) {
            endState = State.state_refresh;
            endY = 0;
        } else if (scrollY < headerHeight) {

            state = State.state_hide_top;
            endState = State.state_idle;
            endY = headerHeight;

        } else if (scrollY < headerHeight + 0.75 * footerHeight) {
            state = State.state_hide_bottom;
            endState = State.state_idle;
            endY = headerHeight;

        } else {
            endState = State.state_load_more;
            endY = scrollMax;

        }
        doChangeStateByAnimation(endY, endState);
    }

    private void onStateChange() {
        switch (state) {
            case state_idle:
                progressBar.setVisibility(GONE);
                tvRefresh.setText("下拉刷新");
                break;
            case state_refresh:
                progressBar.setVisibility(VISIBLE);
                tvRefresh.setText("正在刷新");
                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
                break;
        }
    }



    public void setRefresh(boolean refresh) {
        if (!refresh) {
            tvRefresh.setText("刷新成功");
            progressBar.setVisibility(GONE);
            postDelayed(()->{
                doChangeStateByAnimation(headerHeight, State.state_idle);
            },500);

        }
    }

    RefreshListener refreshListener;

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface RefreshListener {
        void onRefresh();
    }

    private void doChangeStateByAnimation(int endY, final State endState) {
        if (endY == getScrollY()) {
            state = State.state_idle;
            return;
        }
        ValueAnimator animator = ValueAnimator.ofInt(getScrollY(), endY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int sy = (int) animation.getAnimatedValue();
                scrollTo(0, sy);
            }
        });
        animator.setDuration(250);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                state = endState;
                onStateChange();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                state = endState;
                onStateChange();
            }
        });
        animator.start();

    }

    @Override
    public void scrollTo(int x, int y) {
        if (y > scrollMax) {
            y = scrollMax;
        }
        if (y < 0) {
            y = 0;
        }

        super.scrollTo(x, y);
    }

    ScrollContent scrollContent;

    public void setScrollContent(ScrollContent scrollContent) {
        this.scrollContent = scrollContent;
    }

    public interface ScrollContent {
        boolean canScrollUp();

        boolean canScrollDown();
    }

    private enum State {
        state_idle,
        state_load_more,
        state_refresh,
        state_hide_top,
        state_hide_bottom;
    }
}
