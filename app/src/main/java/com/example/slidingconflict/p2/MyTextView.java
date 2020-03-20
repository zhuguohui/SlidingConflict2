package com.example.slidingconflict.p2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.slidingconflict.R;

public class MyTextView extends View {

    private  Bitmap bitmapRed;
    private Paint paint;
    private Bitmap bitmapBlack;

    public MyTextView(Context context) {
        super(context,null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
      //  paint.setColorFilter(new PorterDuffColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP));
    //    bitmapBlack= BitmapFactory.decodeResource(context.getResources(), R.drawable.alipay);
       bitmapBlack = createBitMap(400, 200, Color.BLACK);
    //    bitmapBlack= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_1);
    //    bitmapRed= BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        bitmapRed=createBitMap(120,200,Color.RED);

        BitmapShader bitmapShader1=new BitmapShader(bitmapBlack, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        BitmapShader bitmapShader2=new BitmapShader(bitmapRed, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        Shader shader1=new ComposeShader(bitmapShader2,bitmapShader1, PorterDuff.Mode.DST_OVER);
        paint.setShader(shader1);
//        paint.setColor(Color.YELLOW);
        paint.setTextSize(100);
        paint.setAntiAlias(true);
      paint.setTextSize(100);

    }

    private Bitmap createBitMap(int width, int height, int color) {
        Bitmap bitmap=Bitmap.createBitmap(width+1,height+1, Bitmap.Config.ARGB_8888);

//        bitmap.eraseColor(color);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setColor(color);
        canvas.drawRect(0,0,width,height,paint);

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // canvas.drawColor(Color.WHITE);
        int i = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawText("头条新闻",0,150,paint);
        canvas.restoreToCount(i);
   //    canvas.drawBitmap(bitmapBlack,0,0,paint);
        //canvas.drawBitmap(bitmapRed,0,0,paint);
      // canvas.drawRect(0,0,getWidth(),getHeight(),paint);
//        canvas.drawBitmap(bitmapRed,0,0,paint);

    }


}
