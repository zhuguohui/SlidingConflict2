package com.example.slidingconflict.p1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView implements MyScrollView.ScrollTarget {

    public MyRecyclerView(@NonNull Context context) {
        super(context,null);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
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
        mScroller.fling(getScrollX(),getScrollY(),0,vY,0,getWidth(),0,getHeight());
        isDoFling=true;
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollBy((int)(mScroller.getCurrX()-getX()), (int) (mScroller.getCurrY()-getY()));
            postInvalidate();
        }
    }
}
