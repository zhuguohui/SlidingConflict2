package com.example.slidingconflict.p7;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class FlowLayout extends ViewGroup {
    int padding = 20;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    List<Rect> childPoints = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        int mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        int mWidth = mWidthSize;
        int mHeight = padding;

        int lineNumber = 0;
        int lineUnUseWidth = mWidthSize - padding;
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        childPoints.clear();
        int lastChildHeight = 0;
        int lineMax = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            LayoutParams layoutParams = childView.getLayoutParams();
            int childWidthMeasureSpec;

            if (layoutParams.width > 0) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                Log.i("zzz", "width=" + layoutParams.width + " getMeasuredHeight=" + childView.getMeasuredHeight());
                //指定高度判断当前行是否能放下
                if (layoutParams.width + padding > lineUnUseWidth) {
                    if (childView.getMeasuredHeight() > lineMax) {
                        lineMax = childView.getMeasuredHeight();
                    }
                    //另起一行
                    mHeight += padding;
                    mHeight += lineMax;
                    lineMax = 0;

                    lineUnUseWidth = mWidth - padding;
                    lineMax = savePoint(mWidth, lineUnUseWidth, mHeight, layoutParams.width, childView.getMeasuredHeight(), lineMax);
                    lineUnUseWidth-=childView.getMeasuredWidth();

                } else {

                    lineMax = savePoint(mWidth, lineUnUseWidth, mHeight, layoutParams.width, childView.getMeasuredHeight(), lineMax);
                    // 减去使用的宽度
                    lineUnUseWidth -= layoutParams.width;
                    lineUnUseWidth -= padding;

                }
            } else if (layoutParams.width == LayoutParams.WRAP_CONTENT) {
                //第一次测量,获取期望的宽度
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

                int measuredWidth = childView.getMeasuredWidth();
                if (measuredWidth + padding <= lineUnUseWidth) {
                    //当前行可以放下
                    lineMax = savePoint(mWidth, lineUnUseWidth, mHeight, measuredWidth, childView.getMeasuredHeight(), lineMax);
                    lineUnUseWidth -= measuredWidth;
                    lineUnUseWidth -= padding;

                } else {
                    //另起一行
                    lineUnUseWidth = mWidth - padding;
                    mHeight += lineMax + padding;
                    lineMax = 0;
                    //当前行可以放下

                    lineMax = savePoint(mWidth, lineUnUseWidth, mHeight, measuredWidth, childView.getMeasuredHeight(), lineMax);

                    //减去已经使用的空间
                    lineUnUseWidth -= measuredWidth;
                    lineUnUseWidth -= padding;
                }
                if (i == getChildCount() - 1) {
                    lastChildHeight = childView.getMeasuredHeight();
                }
            }

        }
        mHeight += lastChildHeight;
        mHeight += padding;


        setMeasuredDimension(mWidthSize, mHeight);
    }

    private int savePoint(int mWidth, int lineUnUseWidth, int mHeight, int measuredWidth, int measureHeight, int lineMaxHeight) {
        Rect rect = new Rect();
        rect.left = mWidth - lineUnUseWidth;
        rect.top = mHeight;
        rect.right = rect.left + measuredWidth;
        rect.bottom = rect.top + measureHeight;
        childPoints.add(rect);
        return Math.max(measureHeight, lineMaxHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            Rect rect = childPoints.get(i);
            getChildAt(i).layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }
}
