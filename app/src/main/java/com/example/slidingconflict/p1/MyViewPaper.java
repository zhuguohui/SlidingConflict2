package com.example.slidingconflict.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyViewPaper extends ViewPager implements MyScrollView.ScrollTarget {
    private static final String TAG = MyViewPaper.class.getSimpleName();

    Scroller mScroller;
    boolean isDoFling=false;
    public MyViewPaper(@NonNull Context context) {
        super(context,null);
    }

    public MyViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller =new Scroller(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG,"dispatchTouchEvent ev.action="+ev.getAction()+" y="+ev.getY());
        return super.dispatchTouchEvent(ev);
    }



    @Override
    public void doFling(int vY) {
//        mScroller.fling(getScrollX(),getScrollY(),0,vY,0,getWidth(),0,getHeight());
//        isDoFling=true;
//        postInvalidate();
        for(int i=0;i<getChildCount();i++){
            View childAt = getChildAt(i);
            if(childAt instanceof MyScrollView.ScrollTarget){
                ((MyScrollView.ScrollTarget)childAt).doFling(vY);
            }
        }
    }


}
