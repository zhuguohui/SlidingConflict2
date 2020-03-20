package com.example.slidingconflict.p3;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slidingconflict.R;

public class StickTabLayout2 extends LinearLayout implements NestedScrollingChild {

    private final int minVelocity;
    private final int maxVelocity;
    private int topHeight;
    private int touchSlop;
    private static final String TAG = StickTabLayout2.class.getSimpleName();
    private OverScroller scroller;
    private NestedScrollingParentHelper parentHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StickTabLayout2(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StickTabLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        scroller = new OverScroller(context);
        velocityTracker = VelocityTracker.obtain();
        parentHelper = new NestedScrollingParentHelper(this);
        setNestedScrollingEnabled(true);
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
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {

        if ((nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            return true;
        }
        return false;

    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        parentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        Log.i(TAG, "onNestedPreScroll dy=" + dy);
        if (dy > 0) {
            //向下滑动
            int maxDy = topHeight - getScrollY();
            if (maxDy > 0) {
                if (Math.abs(dy) >= maxDy) {
                    consumed[1] = maxDy;
                } else {
                    consumed[1] = dy;
                }
                scrollBy(0, consumed[1]);
            }
        } else {


            if (!scrollUpTarget.canScrollUp()) {
                if (getScrollY() > 0) {
                    if (Math.abs(dy) >= getScrollY()) {
                        consumed[1] = getScrollY();
                    } else {
                        consumed[1] = dy;
                    }
                    scrollBy(0, consumed[1]);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        parentHelper.onStopNestedScroll(child);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY > 0) {
            if (getScrollY() > 0) {
                //向上
                scroller.fling(0, getScrollY(), 0, (int) velocityY, 0, 0, 0, topHeight);
                return true;
            }

        } else {
            //向下
            if (!scrollUpTarget.canScrollUp() && getScrollY() <= topHeight) {
                scroller.fling(0, getScrollY(), 0, (int) velocityY, 0, 0, 0, topHeight);
                return true;
            }
            //向上滑动
            boolean canScrollUp = false;

            if (target instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) target;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) recyclerView.getChildAt(0).getLayoutParams();
                int position = layoutParams.getViewAdapterPosition();
                if (position <= 10) {
                    canScrollUp = true;
                }
            }
            if(canScrollUp){
                //滑动到顶部
                if(getScrollY()>0){
                    scroller.fling(0,getScrollY(),0, (int) velocityY/2,0,0,0,topHeight);
                }
            }
        }

        return false;
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent action=" + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            scroller.abortAnimation();
        }
        return super.dispatchTouchEvent(ev);
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
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                downX = ev.getX();
                velocityTracker.clear();
                moveMode = false;
                scroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - downY;
//                Log.i(TAG, "onTouchEvent move dy=" + dy);
                scrollBy(0, (int) -dy);
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float yVelocity = velocityTracker.getYVelocity();
//                Log.i(TAG,"fling yVel="+yVelocity+" min="+minVelocity);
                if (Math.abs(yVelocity) > minVelocity) {
                    scroller.fling(0, getScrollY(), 0, (int) -yVelocity, 0, 0, 0, topHeight);
                }
                velocityTracker.clear();
                break;
            case MotionEvent.ACTION_CANCEL:
                scroller.abortAnimation();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
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
