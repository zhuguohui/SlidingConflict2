package com.example.slidingconflict.p3;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.example.slidingconflict.R;

public class StickTabLayout extends LinearLayout {

    private final int minVelocity;
    private final int maxVelocity;
    private int topHeight;
    private int touchSlop;
    private static final String TAG = StickTabLayout.class.getSimpleName();
    private Scroller scroller;

    public StickTabLayout(Context context) {
        this(context, null);
    }

    public StickTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        scroller = new Scroller(context);
        velocityTracker=VelocityTracker.obtain();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重新测量
        View topView = findViewById(R.id.top_view);
        int tabHeight = findViewById(R.id.tabs).getMeasuredHeight();
        topHeight = topView.getMeasuredHeight();
        int measuredHeight = getMeasuredHeight();
        int viewPaperHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight - tabHeight, MeasureSpec.EXACTLY);
        int viewPaperWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        findViewById(R.id.viewPaper).measure(viewPaperWidthMeasureSpec, viewPaperHeightMeasureSpec);
    }

    float downY;
    float downX;
    boolean moveMode = false;
    VelocityTracker velocityTracker;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                downX = ev.getX();
                velocityTracker .clear();
                moveMode = false;
                scroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(ev);
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;

                //从没有滚动到滚动，第一次变化时要判断滑动是否大于阀值
                float absDx = Math.abs(dx);
                float absDy = Math.abs(dy);
                int scrollY = getScrollY();
                if (absDy > absDx && absDy > touchSlop) {
                    if (dy < 0 && scrollY < topHeight) {
                        intercept = true;//向下滑动
                    }
                    if (dy > 0 && scrollUpTarget != null && !scrollUpTarget.canScrollUp() && scrollY > 0) {
                        intercept = true;//向上滑动
                    }
                }
        }
        return intercept;
    }


    interface ScrollUpTarget {
        boolean canScrollUp();
    }

    ScrollUpTarget scrollUpTarget;

    public void setScrollUpTarget(ScrollUpTarget scrollUpTarget) {
        this.scrollUpTarget = scrollUpTarget;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "onTouchEvent event action=" + ev.getAction() + " y=" + ev.getY());
        velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - downY;
//                Log.i(TAG, "onTouchEvent move dy=" + dy);
                scrollBy(0, (int) -dy);
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000,maxVelocity);
                float yVelocity = velocityTracker.getYVelocity();
//                Log.i(TAG,"fling yVel="+yVelocity+" min="+minVelocity);
                if (Math.abs(yVelocity) > minVelocity) {
                    scroller.fling(0, getScrollY(), 0, (int)- yVelocity, 0, 0, 0, topHeight);
                }
                velocityTracker.clear();
                break;
            case  MotionEvent.ACTION_CANCEL:
                scroller.abortAnimation();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(0,scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
//        Log.i(TAG,"scrollTo x="+x+" y="+y);
        if (y > topHeight) {
            y = topHeight;
        }
        if (y < 0) {
            y = 0;
        }

        super.scrollTo(x, y);
    }
}
