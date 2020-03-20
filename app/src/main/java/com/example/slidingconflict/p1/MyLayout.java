package com.example.slidingconflict.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLayout extends LinearLayout {
    private static final String TAG = MyLayout.class.getSimpleName();

    public MyLayout(Context context) {
        super(context, null);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int measureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST);

        getChildAt(1).measure(widthMeasureSpec, measureSpec);//对第二个控件重新测量;

        int measuredHeight = getChildAt(1).getMeasuredHeight();
        measuredHeight += getChildAt(0).getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        getChildAt(1).getLayoutParams();
    }

    int touchDownY, touchDownX;
    MotionEvent downEvent;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean intercept = true;
        Log.i(TAG, "dispatchTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            touchDownY = (int) ev.getY();
            touchDownX = (int) ev.getX();
//            MotionEvent motionEvent=MotionEvent.obtain(ev.getDownTime(),ev.getEventTime(),ev.getAction(),)
            MotionEvent obtain = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(), ev.getAction(), ev.getX(), ev.getY(), ev.getMetaState());
            downEvent = obtain;
            getParent().requestDisallowInterceptTouchEvent(true);

        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int mX = (int) (ev.getX() - touchDownX);
            int mY = (int) (ev.getY() - touchDownY);
            if (Math.abs(mX) < Math.abs(mY)) {
                if (isOpen()) {
                    //纵向滑动,且是向上滑动，如果第一级
                    //     onTouchEvent(downEvent);

                    ((ViewGroup) getParent()).onTouchEvent(downEvent);
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    if (mY > 0 && isTopListener != null && isTopListener.isTop()) {
                        //向下滑动，如果
                        //   onTouchEvent(downEvent);
                        ((ViewGroup) getParent()).onTouchEvent(downEvent);
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                }

            }

        }

        super.dispatchTouchEvent(ev);
        return intercept;
    }

    private MotionEvent copyEvent(MotionEvent ev, int x) {
        MotionEvent obtain = MotionEvent.obtain(ev.getDownTime(), ev.getEventTime(), ev.getAction(), x, ev.getY(), ev.getMetaState());
        return obtain;
    }

    IsTopListener isTopListener;

    public void setIsTopListener(IsTopListener isTopListener) {
        this.isTopListener = isTopListener;
    }

    public interface IsTopListener {
        boolean isTop();
    }

    boolean isOpen() {
        ViewGroup parent = (ViewGroup) getParent();
        int scrollY = parent.getScrollY();
        int height = getChildAt(0).getHeight();
        return scrollY < height;
    }

}
