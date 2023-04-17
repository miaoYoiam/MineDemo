package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Day：2022/8/3 4:29 下午
 * @author zhanglei
 *
 *  徽章+数字角标 一排
 */
class WealthLevelDrawableV2(val context: Context, val lv: Int, val height: Float) : Drawable() {
    private val leftRect by lazy {
        Rect()
    }
    private val rightRect by lazy {
        Rect()
    }

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private var gradient: LinearGradient? = null

    init {
        leftRect.set(0, 0, height.toInt(), height.toInt())

        val rightWidth = WealthLevelDrawableHelper.getRectWidthForV2(height, lv).toInt()
        val rightHeight = WealthLevelDrawableHelper.getRectHeightForV2(height).toInt()
        val left = WealthLevelDrawableHelper.getRectLeftBoundForV2(height).toInt()
        val right = left + rightWidth
        val top = (height - rightHeight) / 2f
        val bottom = top + rightHeight
        rightRect.set(left, top.toInt(), right, bottom.toInt())

        bounds.right = left + rightRect.width()
        bounds.bottom = height.toInt()
        super.setBounds(bounds)

        WealthLevelDrawableHelper.getLinearGradientColorArrayForV2(context, lv)?.let {
            gradient = LinearGradient(0f, 0f, bounds.right.toFloat(), bounds.bottom.toFloat(), it, null, Shader.TileMode.CLAMP)
        }
    }

    override fun getIntrinsicWidth(): Int {
        return bounds.width()
    }

    override fun draw(canvas: Canvas) {
        super.setBounds(bounds)
        val bitmap = WealthLevelDrawableHelper.getLevelBitmap(context, lv)
        if (bitmap != null) {
            //渐变
            val round = WealthLevelDrawableHelper.getRectRoundForV2(height)
            gradient?.let {
                mPaint.shader = it
                mPaint.style = Paint.Style.FILL
                canvas.drawRoundRect(rightRect.left.toFloat(), rightRect.top.toFloat(), rightRect.right.toFloat() - mPaint.strokeWidth / 2, rightRect.bottom.toFloat(), round, round, mPaint)
            }

            //边框
            WealthLevelDrawableHelper.getRectStrokeColorForV2(context, lv)?.let {
                mPaint.shader = null
                mPaint.color = it
                mPaint.style = Paint.Style.STROKE
                mPaint.strokeWidth = WealthLevelDrawableHelper.getRectStrokeSizeForV2(height)
                canvas.drawRoundRect(rightRect.left.toFloat(), rightRect.top.toFloat(), rightRect.right.toFloat() - mPaint.strokeWidth / 2, rightRect.bottom.toFloat(), round, round, mPaint)
            }

            //icon
            canvas.drawBitmap(bitmap, null, leftRect, mPaint)

            //文字
            val text = lv.toString()
            mPaint.typeface = Typeface.DEFAULT_BOLD
            mPaint.textSize = WealthLevelDrawableHelper.getTextSizeForV2(height)
            mPaint.color = Color.WHITE
            mPaint.shader = null
            mPaint.style = Paint.Style.FILL
            val textWidth = mPaint.measureText(text)
            val x = bounds.right.toFloat() - textWidth - WealthLevelDrawableHelper.getTextRightBoundForV2(height)
            val fontMetrics = mPaint.fontMetrics
            val y = rightRect.centerY() + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
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