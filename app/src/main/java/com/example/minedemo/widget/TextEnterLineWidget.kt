package com.example.minedemo.widget

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.example.minedemo.R
import com.example.minedemo.utils.DisplayUtil

class TextEnterLineWidget : View {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        0
    ) {
        initView()
    }

    private lateinit var textPaint: TextPaint

    private fun initView() {
        textPaint = TextPaint().apply {
            color = ContextCompat.getColor(context, R.color.black)
            textSize = DisplayUtil.dp2px(15f)
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val layout = StaticLayout(
            "哈哈哈哈哈哈哈发送到农夫哈哈哈哈哈哈哈发送到农夫哈哈哈哈哈哈哈发送到农夫哈哈哈哈哈哈哈发送到农夫哈哈哈哈哈哈哈发送到农夫",
            textPaint,
            DisplayUtil.dp2px(200f).toInt(),
            Layout.Alignment.ALIGN_NORMAL,
            1f,
            0f,
            true
        )

        canvas?.save()
        canvas?.translate(DisplayUtil.dp2px(50f), 0f)
        layout.draw(canvas)
        canvas?.restore()
    }
}