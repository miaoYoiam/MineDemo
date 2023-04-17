package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.example.minedemo.R
import com.example.minedemo.utils.DisplayUtil

class CircleAvatarWidget : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfermode: Xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var bitmap: Bitmap
    private val savedArea = RectF()

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        0
    ) {
        bitmap = getBitmap(WIDTH.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        savedArea.set(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val saved = canvas?.saveLayer(savedArea, paint)
        canvas?.drawOval(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH, paint)
        paint.xfermode = xfermode
        canvas?.drawBitmap(bitmap, PADDING, PADDING, paint)
        paint.xfermode = null
        if (saved != null) {
            canvas.restoreToCount(saved)
        }
    }

    fun getBitmap(mWidth: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.mipmap.avatar_9, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = mWidth
        return BitmapFactory.decodeResource(resources, R.mipmap.avatar_9, options)
    }

    companion object {
        var WIDTH = DisplayUtil.dp2px(300f)
        var PADDING = DisplayUtil.dp2px(50f)
        var EDGE_WIDTH = DisplayUtil.dp2px(10f)
    }
}