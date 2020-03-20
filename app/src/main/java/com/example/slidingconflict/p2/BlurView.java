package com.example.slidingconflict.p2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

import com.example.slidingconflict.R;

import static android.content.ContentValues.TAG;

public class BlurView extends View {

    private Bitmap bitmap2;
    private Paint paint;
    private Bitmap bitmap1;
    private float[] pointsSrc;
    private float[] pointsDst;
    private Camera camera;
    private float deg = 0;

    public BlurView(Context context) {
        super(context, null);
    }

    public BlurView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setMaskFilter(new BlurMaskFilter(200, BlurMaskFilter.Blur.NORMAL));
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_1);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Log.i("zzz", "width=" + bitmap1.getWidth() + " height=" + bitmap1.getHeight());
        //  bitmap=rsBlur(context,bitmap,5);

        float right = bitmap1.getWidth();
        float left = 0;
        float top = 0;
        float bottom = bitmap1.getHeight();
        pointsSrc = new float[]{left, top, right, top, left, bottom, right, bottom};
        pointsDst = new float[]{left - 10, top + 50, right + 120, top - 90, left + 20, bottom + 30, right + 20, bottom + 60};
        camera = new Camera();
        deg = 0;
        //     postDelayed(showRun,1000);
        paint.setTextSize(30);

    }

    Runnable showRun = new Runnable() {
        @Override
        public void run() {
            deg++;
            if (deg > 360) {
                deg = 0;
            }
            invalidate();
            postDelayed(showRun, 30);
        }
    };

    @SuppressLint("NewApi")
    private static Bitmap rsBlur(Context context, Bitmap source, int radius) {

        Bitmap inputBmp = source;
        //(1)
        RenderScript renderScript = RenderScript.create(context);

        Log.i(TAG, "scale size:" + inputBmp.getWidth() + "*" + inputBmp.getHeight());

        // Allocate memory for Renderscript to work with
        //(2)
        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        //(3)
        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //(4)
        scriptIntrinsicBlur.setInput(input);
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius);
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output);
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp);
        //(8)
        renderScript.destroy();

        return inputBmp;
    }

   /* @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.clipRect(0,0,100,100);
        Path path=new Path();
        int radius = bitmap1.getWidth() / 2;
        int cx=bitmap1.getWidth()/2+100;
        int cy=bitmap1.getHeight()/2;
        path.addCircle(cx,cy,radius, Path.Direction.CW);
        canvas.save();
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap1, 100, 0, paint);
        canvas.restore();
        canvas.drawBitmap(bitmap2, 100, bitmap1.getHeight()+100, paint);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        int itemWidth = getWidth() / 3;
        int itemHeight = getHeight() / 3;
        int index=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                canvas.save();
                canvas.clipRect(j*itemWidth,i*itemHeight,(j+1)*itemHeight,(i+1)*itemHeight);
                canvas.translate(j*itemWidth,i*itemHeight);
                drawChild(canvas,index);
                canvas.restore();
                index++;
            }
        }
    }

    int[] colors = new int[]{Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN};

    private void drawChild(Canvas canvas, int index) {
        canvas.drawColor(colors[index % colors.length]);
        canvas.drawText("第" + index + "个", 0, 100, paint);
    }

}
