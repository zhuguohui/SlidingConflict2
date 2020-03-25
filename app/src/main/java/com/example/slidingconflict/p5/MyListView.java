package com.example.slidingconflict.p5;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyListView extends ViewGroup {
    //    ListView
    private static final String TAG = MyListView.class.getSimpleName();

    Recycler recycler = new Recycler();

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int mLastPosition = 0;
    int mStartPosition = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (notLayout) {
            notLayout = false;
            return;
        }
        Log.i(TAG, "onLayout");
        removeAllViews();
        int nextTop = t;
        int nextPosition = 0;
        int end = b;
        boolean layoutPlus = false;//是否完成最后一个的绘制
        while (nextPosition < adapter.getCount()) {
            if (nextTop >= b) {
                layoutPlus = true;
            }
            View view = recycler.getView(nextPosition);
            view.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int height = view.getMeasuredHeight();
            int width = view.getMeasuredWidth();
            addViewInLayout(view, -1, new LayoutParams(width, height));
            view.layout(0, nextTop, width, nextTop + height);
            view.setTag(nextPosition);
            nextPosition++;
            nextTop += height;
            if (layoutPlus) break;

        }
        mLastPosition = nextPosition - 1;
    }

    int offsetY;

    float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getY() - downY);
                offsetY += dy;
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).offsetTopAndBottom(dy);
                }

                downY = event.getY();
                recyclerView();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    boolean notLayout;

    private void recyclerView() {
        View childAt = findViewWithTag(mStartPosition);
        int bottom = childAt.getBottom();

        if (bottom < 0) {
            detachViewFromParent(childAt);
            //recycler.recyclerView(childAt);

            attachViewToParent(childAt, -1, childAt.getLayoutParams());
            int lastTop = findViewWithTag(mLastPosition).getBottom();
            int dy = lastTop - childAt.getTop();
            childAt.offsetTopAndBottom(dy);
            childAt.setTag(mLastPosition + 1);
            notLayout = true;
            adapter.updateView(childAt, mLastPosition + 1);
            childAt.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            childAt.layout(childAt.getLeft(), childAt.getTop(), childAt.getLeft() + childAt.getMeasuredWidth(), childAt.getTop() + childAt.getMeasuredHeight());

//            childAt.requestLayout();
//            childAt.invalidate();

            mStartPosition++;
            mLastPosition++;
            //     invalidate();
        }
        Log.i(TAG,"recyclerView: count="+getChildCount());
    }


    Adapter adapter;

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            removeAllViews();
            requestLayout();
            invalidate();
        }
    }

    private class Recycler {
        List<View> detachViewList = new ArrayList<>();
        List<View> activViewList = new ArrayList<>();

        View getView(int position) {
            View view1 = findViewWithTag(position);
            if (view1 != null) return view1;

            if (detachViewList.size() > 0) {
                View view = detachViewList.remove(detachViewList.size() - 1);
                adapter.updateView(view, position);
                return view;
            } else {
                View view = adapter.getView(MyListView.this);
                adapter.updateView(view, position);
                return view;
            }
        }

        public void recyclerView(View childAt) {
            detachViewList.add(childAt);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw");
    }

    public interface Adapter {
        int getCount();

        View getView(MyListView myListView);

        void updateView(View view, int position);
    }
}
