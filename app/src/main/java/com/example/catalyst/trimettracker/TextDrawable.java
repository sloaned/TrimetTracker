package com.example.catalyst.trimettracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Dan on 7/10/2016.
 */
public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;
    private Context mContext;

    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    public TextDrawable( Context context, String text) {

        mContext = context;
        this.text = text;

        this.paint = new Paint();
        paint.setColor(ContextCompat.getColor(mContext, R.color.ring_border));
        paint.setTextSize(20f);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
       // paint.setShadowLayer(6f, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);

        mIntrinsicHeight = (int) (paint.measureText(text, 0, text.length()) + 20);
        mIntrinsicWidth = (int) (paint.measureText(text, 0, text.length()) + 40);
    }

    @Override
    public void draw(Canvas canvas) {

        Rect bounds = getBounds();
        canvas.drawText(text, 0, text.length(),
                bounds.centerX(), bounds.centerY(), paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
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