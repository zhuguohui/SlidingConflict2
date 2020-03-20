package com.example.slidingconflict.p2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SOLOScrollView extends View {


    private TextPaint paint;
    private String info;
    private StaticLayout staticLayout;

    public SOLOScrollView(Context context) {
        super(context,null);
    }

    int donwY=0;
    int offsetY=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                donwY= (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy= (int) (event.getY()-donwY);
                offsetY+=dy;
                donwY= (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    public SOLOScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);
        info = "StaticLayout 的构造方法是 StaticLayout(CharSequence source, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad)，其中参数里：\n" +
                "\n" +
                "width 是文字区域的宽度，文字到达这个宽度后就会自动换行；\n" +
                "align 是文字的对齐方向；\n" +
                "spacingmult 是行间距的倍数，通常情况下填 1 就好；\n" +
                "spacingadd 是行间距的额外增加值，通常情况下填 0 就好；\n" +
                "includepad 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界。\n" +
                "\n" +
                "如果你需要进行多行文字的绘制，并且对文字的排列和样式没有太复杂的花式要求，那么使用 StaticLayout 就好。";
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        staticLayout = new StaticLayout(info,paint,getWidth(),Layout.Alignment.ALIGN_NORMAL,1,0,true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(0,offsetY);
        drawSelf(canvas);
        canvas.restore();
    }

    protected void drawSelf(Canvas canvas){
        staticLayout.draw(canvas);
    }
}
