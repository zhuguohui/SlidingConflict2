package com.example.slidingconflict.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {


    private static final String TAG = MyScrollView.class.getSimpleName();
    private int scrollY;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        }
        return true;
    }


    /*
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            Log.i(TAG,"onLayout");
            if(!changed){
                scrollY = getScrollY();
            }else{
                scrollY=0;
            }
            super.onLayout(changed, l, t, r, b);
            scrollTo(0,scrollY);
        }*/
    boolean downEventPost = false;
    int downY;

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {

        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    VelocityTracker velocityTracker;
    boolean needScrollChild = false;
    ScrollTarget mScrollTarget;
    float mVelocityY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downEventPost = false;
                // downY-=600;
                break;
            case MotionEvent.ACTION_MOVE:

                Log.i(TAG, "move scrollY=" + getScrollY());
                int dy = (int) (ev.getY() - downY);
                if (dy < 0 && getScrollY() == 600) {
                    if (!downEventPost) {
                        int actionOld = ev.getAction();
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        ((ViewGroup) getChildAt(0)).getChildAt(1).dispatchTouchEvent(ev);
                        ev.setAction(actionOld);
                        downEventPost = true;
                    } else {
                        ((ViewGroup) getChildAt(0)).getChildAt(1).dispatchTouchEvent(ev);
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                velocityTracker.computeCurrentVelocity(1000);
                float vY = velocityTracker.getYVelocity();
                if (Math.abs(vY) > 1000) {
                    View childAt = ((ViewGroup) getChildAt(0)).getChildAt(1);

                    if (childAt instanceof ScrollTarget) {
                        needScrollChild = true;
                        mScrollTarget = (ScrollTarget) childAt;
                        mVelocityY = vY;
                    }
                }
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
//                doFling(vY);
                break;
        }
        return super.onTouchEvent(ev);
    }



    @Override
    public void computeScroll() {
        super.computeScroll();

        if (getScrollY() == 600 && needScrollChild) {
            mScrollTarget.doFling((int) mVelocityY);
            needScrollChild = false;
        }
    }

    private void doFling(float vy) {
        Log.i(TAG, "doFling vy=" + vy);
        if (vy < 100) {
            return;
        }
        if (vy < 0) {
            //向上滑动
        } else {

        }
    }

    public interface ScrollTarget {
        void doFling(int vY);
    }
}
