package com.example.slidingconflict.p8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MyColorTextView extends androidx.appcompat.widget.AppCompatTextView {
    Paint paint;
    public MyColorTextView(Context context) {
        super(context);
    }

    public MyColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       // setMeasuredDimension(200,200);
        @SuppressLint("DrawAllocation") Shader shader = new LinearGradient(getMeasuredWidth()/2, 0, getMeasuredWidth()/2, getMeasuredHeight(), Color.parseColor("#F6B708"),
                Color.parseColor("#E03625"), Shader.TileMode.CLAMP);
        getPaint().setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       // canvas.drawText("新闻",0,100,paint);
    }
}
