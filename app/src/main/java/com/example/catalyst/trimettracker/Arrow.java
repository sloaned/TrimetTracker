package com.example.catalyst.trimettracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by dsloane on 7/11/2016.
 */
public class Arrow extends Drawable {

    private final String TAG = Arrow.class.getSimpleName();

    private Context mContext;
    private String text;
    private int bearing;

    private Path path;
    private Paint paint;

    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    public Arrow(Context context, String text, int bearing) {

        mContext = context;
        this.text = text;
        this.bearing = bearing;
        this.paint = new Paint();
        this.path = new Path();

        paint.setColor(ContextCompat.getColor(mContext, R.color.arrow));
        paint.setStyle(Paint.Style.FILL);

        mIntrinsicHeight = (int) (text.length() + 160);
        mIntrinsicWidth = (int) (text.length() + 160);

    }

    @Override
    public void draw(Canvas canvas) {

        float cosine = (float) Math.cos(Math.toRadians((double) bearing));
        float sine = (float) Math.sin(Math.toRadians((double) bearing));
        float tangent = (float) Math.tan(Math.toRadians((double) bearing));

        float arrowTangent = (float) Math.tan(Math.toRadians(40.0));
        float arrowCotangent = (float) Math.atan(Math.toRadians(40.0));

        Log.d(TAG, "cosine = " + cosine + ", sine = " + sine);

        path.moveTo((float) mIntrinsicWidth-80, (float) mIntrinsicHeight-80);

        if (bearing <= 90) {
            /*
            float x1 = (float) mIntrinsicWidth-80 - (sine*4);
            float y1 = (float) mIntrinsicHeight-80 + (cosine*4);
            float x2 = x1 + (70f * cosine);
            float y2 = y1 - (70f * sine);
            float x3 = x2 - (8f * cosine);
            float y3 = y2 - (12f * sine);
            float x4 = x3 + (8f * cosine);
            float y4 = y3 - (8f * sine);
            float x5 = x4 + (10f * cosine);
            float y5 = y4 + (16f * sine);
            float x6 = x5 - (10f * cosine);
            float y6 = y5 + (16f * sine);
            float x7 = x6 - (8f * cosine);
            float y7 = y6 + (8f * sine);
            float x8 = x7 + (8f * cosine);
            float y8 = y7 - (12f * sine);
            float x9 = x8 - (70f * cosine);
            float y9 = y8 + (70f * sine);  */

            float x1 = (float) mIntrinsicWidth-80 - (sine*4);
            float y1 = (float) mIntrinsicHeight-80 - (cosine*4);
            float x2 = x1 + (70f * cosine);
            float y2 = y1 - (70f * sine);
            float x3 = x2 - (12f * sine);
            float y3 = y2 - (12f * cosine);
            float x4 = x3 + (8f * cosine);
            float y4 = y3 - (8f * sine);
            float x5 = x4 + (16f * cosine);
            float y5 = y4 + (16f * sine);
            float x6 = x5 + (16f * sine);
            float y6 = y5 + (16f * cosine);
            float x7 = x6 - (8f * cosine);
            float y7 = y6 + (8f * sine);
            float x8 = x7 + (12f * sine);
            float y8 = y7 - (12f * cosine);
            float x9 = x8 - (70f * cosine);
            float y9 = y8 + (70f * sine);


            path.lineTo(x1, y1);
            path.lineTo(x2, y2);
            path.lineTo(x3, y3);
            path.lineTo(x4, y4);
            path.lineTo(x5, y5);
            path.lineTo(x6, y6);
            path.lineTo(x7, y7);
            path.lineTo(x8, y8);
            path.lineTo(x9, y9);
            path.lineTo(x1, y1);
            path.close();



        }



       // path.moveTo((float) mIntrinsicWidth-80, (float) mIntrinsicHeight-76);
     /*   path.moveTo((float) mIntrinsicWidth-80, (float) mIntrinsicHeight-76);
        path.lineTo((float) mIntrinsicWidth-10, (float) mIntrinsicHeight-76);
        path.lineTo((float) mIntrinsicWidth-18, (float) mIntrinsicHeight-68);
        path.lineTo((float) mIntrinsicWidth-10, (float) mIntrinsicHeight-68);
        path.lineTo((float) mIntrinsicWidth, (float) mIntrinsicHeight-80);
        path.lineTo((float) mIntrinsicWidth-10, mIntrinsicHeight-92);
        path.lineTo((float) mIntrinsicWidth-18, mIntrinsicHeight-92);
        path.lineTo((float) mIntrinsicWidth-10, mIntrinsicHeight-84);
        path.lineTo((float) mIntrinsicWidth-80, mIntrinsicHeight-84);
        path.lineTo((float) mIntrinsicWidth-80, (float) mIntrinsicHeight-76);
        path.close();

*/
      //  path.addRect((float) mIntrinsicWidth-80, (float) mIntrinsicHeight-20, (float) mIntrinsicWidth, (float) mIntrinsicHeight-12, Path.Direction.CCW);


        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }
    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }
}
