package com.example.minedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

/**
 * Day：2021/12/23 5:55 下午
 *
 * @author zhanglei
 */
public class RoundFrameLayout  extends FrameLayout {
    private Path mPath;
    private Paint mPaint;
    private RectF mRectf;
    private float[] mMatrix;

    public RoundFrameLayout(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPath = new Path();
        this.mRectf = new RectF();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.mMatrix = new float[8];
    }

    public void setRadius(float var1, float var2, float var3, float var4) {
        this.mMatrix[0] = var1;
        this.mMatrix[1] = var1;
        this.mMatrix[2] = var2;
        this.mMatrix[3] = var2;
        this.mMatrix[4] = var4;
        this.mMatrix[5] = var4;
        this.mMatrix[6] = var3;
        this.mMatrix[7] = var3;
        this.postInvalidate();
    }

    public void setRadius(float radius) {
        Arrays.fill(this.mMatrix, radius);
        this.postInvalidate();
    }

    public void setTopLeftRadius(float topLeft) {
        this.mMatrix[0] = topLeft;
        this.mMatrix[1] = topLeft;
        this.postInvalidate();
    }

    public void setTopRightRadius(float topRight) {
        this.mMatrix[2] = topRight;
        this.mMatrix[3] = topRight;
        this.postInvalidate();
    }

    public void setBottomRightRadius(float bottomRight) {
        this.mMatrix[5] = bottomRight;
        this.mMatrix[6] = bottomRight;
        this.postInvalidate();
    }

    public void setBottomLeftRadius(float bottomLeft) {
        this.mMatrix[6] = bottomLeft;
        this.mMatrix[7] = bottomLeft;
        this.postInvalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mRectf.set(0.0F, 0.0F, (float)w, (float)h);
    }

    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= 28) {
            canvas.save();
            canvas.clipPath(this.setRadius());
            super.draw(canvas);
        } else {
            canvas.saveLayer(this.mRectf, null, Canvas.ALL_SAVE_FLAG);
            super.draw(canvas);
            canvas.drawPath(this.setRadius(), this.mPaint);
        }

        canvas.restore();
    }

    protected void dispatchDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= 28) {
            canvas.save();
            canvas.clipPath(this.setRadius());
            super.dispatchDraw(canvas);
        } else {
            canvas.saveLayer(this.mRectf, null, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            canvas.drawPath(this.setRadius(), this.mPaint);
        }

        canvas.restore();
    }

    private Path setRadius() {
        this.mPath.reset();
        this.mPath.addRoundRect(this.mRectf, this.mMatrix, Path.Direction.CW);
        return this.mPath;
    }
}