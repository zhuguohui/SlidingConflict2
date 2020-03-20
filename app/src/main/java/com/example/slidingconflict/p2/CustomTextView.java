package com.example.slidingconflict.p2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Locale;

public class CustomTextView extends View {

    private Paint paint;

    public CustomTextView(Context context) {
        super(context,null);

    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paint.setTextLocale(Locale.TAIWAN);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x=getWidth()/2;
        int y=getHeight()/2;
        canvas.drawText("好好学习",x,y,paint);
        Paint.FontMetrics metrics=paint.getFontMetrics();

    }
}
