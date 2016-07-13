package com.example.catalyst.trimettracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
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
    private final Paint textPaint;
    private final Path path;
    private Context mContext;

    private int mIntrinsicWidth;
    private int mIntrinsicHeight;

    public TextDrawable( Context context, String text) {

        mContext = context;
        this.text = text;

        this.paint = new Paint();
        this.path = new Path();
        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        textPaint.setColor(ContextCompat.getColor(mContext, R.color.ring_border));
        textPaint.setTextSize(20f);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        // paint.setShadowLayer(6f, 0, 0, Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);

        mIntrinsicHeight = (int) (paint.measureText(text, 0, text.length()) + 32);//24
        mIntrinsicWidth = (int) (paint.measureText(text, 0, text.length()) + 32);

        path.addCircle(mIntrinsicHeight/2, mIntrinsicHeight/2, mIntrinsicHeight/2, Path.Direction.CCW);

    }

    @Override
    public void draw(Canvas canvas) {

        Rect bounds = getBounds();

        paint.setColor(ContextCompat.getColor(mContext, R.color.ring_background));
        paint.setStyle(Paint.Style.FILL);

        canvas.drawPath(path, paint);

        paint.setColor(ContextCompat.getColor(mContext, R.color.ring_border));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawPath(path, paint);

        int xPos = mIntrinsicWidth/2 - (int) (paint.measureText(text, 0, text.length())*.75);
        int yPos = (int) ((mIntrinsicHeight / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        textPaint.setColorFilter(cf);
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