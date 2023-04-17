package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Day：2022/8/3 2:34 下午
 * @author zhanglei
 *
 * 徽章+数字角标 右下角
 */
class WealthLevelDrawableV1(val context: Context, val lv: Int) : Drawable() {
    private val mRect by lazy {
        Rect()
    }

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    override fun draw(canvas: Canvas) {
        mRect.set(bounds.left, bounds.top, bounds.right, bounds.bottom)
        val bitmap = WealthLevelDrawableHelper.getLevelBitmap(context, lv)
        if (bitmap != null) {
            val width = bounds.width().toFloat()
            val scaleSize = WealthLevelDrawableHelper.getBitmapScaleSizeForV1(width)
            mRect.right = (bounds.right - scaleSize).toInt()
            mRect.bottom = (bounds.bottom - scaleSize).toInt()
            canvas.drawBitmap(bitmap, null, mRect, mPaint)

            val text = lv.toString()
            mPaint.typeface = Typeface.DEFAULT_BOLD
            mPaint.textSize = WealthLevelDrawableHelper.getTextSizeForV1(width)
            mPaint.color = Color.WHITE
            mPaint.style = Paint.Style.FILL
            val textWidth = mPaint.measureText(text)
            val x = bounds.right.toFloat() - textWidth
            val y = bounds.bottom.toFloat() - scaleSize
            canvas.drawText(text, x, y, mPaint)

            mPaint.color = WealthLevelDrawableHelper.getTextColorForV1(context, lv) ?: Color.WHITE
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = WealthLevelDrawableHelper.getStrokeSizeForV1(width)
            canvas.drawText(text, x, y, mPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}