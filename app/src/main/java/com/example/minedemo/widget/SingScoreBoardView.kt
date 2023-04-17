package com.example.minedemo.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.example.minedemo.data.SongExData
import com.example.minedemo.utils.dp

/**
 * Day：2022/7/16 8:23 上午
 * @author zhanglei
 */
class SingScoreBoardView : View {
    private val child: SingScoreBoardViewByCanvas by lazy {
        SingScoreBoardViewByCanvas(context)
    }

    private val data by lazy {
        SongExData()
    }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        child.setRect(600.dp.toInt(), 100.dp.toInt(), 0, 100.dp.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        child.setSkillCount(data)
        child.setSkillMatchCount(data)
        child.onDraw(canvas)
    }
}