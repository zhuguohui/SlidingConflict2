package com.example.slidingconflict.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Scroller;

public class MyScrollView2 extends ScrollView implements MyScrollView.ScrollTarget {
    public MyScrollView2(Context context) {
        super(context,null);
    }

    public MyScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(context);
    }


    private static final String TAG = MyViewPaper.class.getSimpleName();

    Scroller mScroller;
    boolean isDoFling=false;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"dispatchTouchEvent ev.action="+ev.getAction()+" y="+ev.getY());
        return super.dispatchTouchEvent(ev);
    }



    @Override
    public void doFling(int vY) {
        mScroller.fling(getScrollX(),getScrollY(),0,-vY,0,getWidth(),0,getHeight());
        isDoFling=true;
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
