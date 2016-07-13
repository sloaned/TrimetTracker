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

        float arrowCosine = (float) Math.cos(Math.toRadians(140.0 + bearing));
        float arrowSine = (float) Math.sin(Math.toRadians(140.0 + bearing));

        float arrow2Cosine = (float) Math.cos(Math.toRadians(220.0 + bearing));
        float arrow2Sine = (float) Math.sin(Math.toRadians(220.0 + bearing));

        path.moveTo((float) mIntrinsicWidth-80, (float) mIntrinsicHeight-80);

        float x1 = (float) mIntrinsicWidth-80 - (sine*4);
        float y1 = (float) mIntrinsicHeight-80 - (cosine*4);
        float x2 = x1 + (67.233f * cosine);
        float y2 = y1 - (67.233f * sine);
        float x3 = x2 + (15.557f * arrowCosine);
        float y3 = y2 - (15.557f * arrowSine);
        float x4 = x3 + (8f * cosine);
        float y4 = y3 - (8f * sine);
        float x5 = x4 - (20.324f * arrowCosine);
        float y5 = y4 + (20.324f * arrowSine);
        float x6 = x5 + (20.324f * arrow2Cosine);
        float y6 = y5 - (20.324f * arrow2Sine);
        float x7 = x6 - (8f * cosine);
        float y7 = y6 + (8f * sine);
        float x8 = x7 - (15.557f * arrow2Cosine);
        float y8 = y7 + (15.557f * arrow2Sine);
        float x9 = x8 - (67.233f * cosine);
        float y9 = y8 + (67.233f * sine);


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
