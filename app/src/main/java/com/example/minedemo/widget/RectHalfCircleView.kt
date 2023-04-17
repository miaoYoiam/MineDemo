package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.example.minedemo.R
import com.example.minedemo.utils.DisplayUtil

class RectHalfCircleView : View {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        0
    ) {
        mPaint = Paint().apply {
            isAntiAlias = true
            context?.let {
                color = ContextCompat.getColor(it, R.color.black_alpha_30)
            }
            style = Paint.Style.FILL
        }
        hintBitmap = BitmapFactory.decodeResource(resources, R.mipmap.loading_headset)

        textPaint = TextPaint().apply {
            isAntiAlias = true
            context?.let {
                color = ContextCompat.getColor(it, R.color.white_alpha_50)
            }
            textSize = DisplayUtil.dp2px(14f)
        }
    }

    private var mWidth = 0f
    private var mHeight = 0f
    private var mPaint: Paint
    private var mCircleRect: RectF = RectF()
    private var mCircleRadius = 0f
    private var mRectHeight = 0f
    private var mRectRectF: RectF = RectF()

    private var hintBitmap: Bitmap
    private var hintDrawableRectF: RectF = RectF()

    private var textPaint: TextPaint

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        mCircleRadius = mHeight * CIRCLE_RADIUS_RATIO
        mRectHeight = mHeight * RECT_HEIGHT_RATIO

        mCircleRect.apply {
            left = mWidth / 2 - mCircleRadius
            top = 0f
            right = mWidth / 2 + mCircleRadius
            bottom = mCircleRadius * 2 + DisplayUtil.dp2px(1f)
        }
        mRectRectF.apply {
            left = 0f
            top = mCircleRadius
            right = mWidth
            bottom = mHeight
        }
        hintDrawableRectF.apply {
            left = (mWidth - hintBitmap.width) / 2
            top = mCircleRadius - hintBitmap.height / 2
            right = (mWidth + hintBitmap.width) / 2
            bottom = mCircleRadius + hintBitmap.height / 2
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawArc(mCircleRect, 180f, 180f, true, mPaint)

        canvas?.drawRoundRect(
            mRectRectF,
            DisplayUtil.dp2px(8f),
            DisplayUtil.dp2px(8f),
            mPaint
        )

        canvas?.drawBitmap(hintBitmap, null, hintDrawableRectF, mPaint)

        canvas?.save()
        val layout = StaticLayout(
            "演唱過程中，佩戴有線耳機，錄音效果更好，打分會更高哦演唱過程中，佩戴有線耳机",
            textPaint,
            (mWidth - DisplayUtil.dp2px(14f) * 2).toInt(),
            Layout.Alignment.ALIGN_CENTER,
            1f,
            0f,
            true
        )
        canvas?.translate(
            DisplayUtil.dp2px(14f),
            mCircleRadius + (mHeight - mCircleRadius - layout.height) / 2
        )
        layout.draw(canvas)
        canvas?.restore()
    }

    companion object {
        const val CIRCLE_RADIUS_RATIO = 26 / 114f
        const val RECT_HEIGHT_RATIO = 88 / 114f
    }
}