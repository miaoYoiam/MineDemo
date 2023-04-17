package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.minedemo.R

/**
 * Day：2022/10/20 18:27
 * @author zhanglei
 */
class SimpleImageView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val reuseBitmapOptions = BitmapFactory.Options().apply {
        this.inJustDecodeBounds = false
        this.inSampleSize = 1
        this.inMutable = true
    }

    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(
            resources,
            R.drawable.icon_ktv_bg_default,
            reuseBitmapOptions
        )
    }

    private val dstRect by lazy {
        Rect()
    }
    private val srcRect by lazy {
        Rect()
    }

    private val paint by lazy { Paint() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        srcRect.set(0, 0, bitmap.width, bitmap.height)
        dstRect.set(0, 0, measuredWidth, measuredHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cur0 = System.currentTimeMillis()
        canvas?.drawBitmap(bitmap, srcRect, dstRect, null)
        Log.e("draw", "null paint duration:${System.currentTimeMillis() - cur0}")

//        val cur1 = System.currentTimeMillis()
//        canvas?.drawBitmap(bitmap, srcRect, dstRect, paint)
//        Log.e("draw", "use paint duration:${System.currentTimeMillis() - cur1}")
    }
}