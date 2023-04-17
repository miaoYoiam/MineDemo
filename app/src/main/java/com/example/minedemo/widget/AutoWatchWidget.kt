package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.example.minedemo.utils.DisplayUtil
import kotlin.math.cos
import kotlin.math.sin

class AutoWatchWidget : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPath = Path()
    private lateinit var effectPath: PathDashPathEffect


    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        0
    ) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = DisplayUtil.dp2px(2f)
        mPath.addRect(0f, 0f, DisplayUtil.dp2px(2f), DisplayUtil.dp2px(10f), Path.Direction.CW)

        val arc = Path()
        arc.addArc(
            width / 2 - RADIUS, height / 2 - RADIUS, width / 2 + RADIUS, height / 2 + RADIUS,
            90f + ANGLE / 2, 360f - ANGLE
        )
        val pathMeasure = PathMeasure(arc, false)
        effectPath =
            object : PathDashPathEffect(
                mPath,
                (pathMeasure.length - DisplayUtil.dp2px(2f)) / 20f,
                0f,
                Style.ROTATE
            ) {}

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawArc(
            width / 2 - RADIUS, height / 2 - RADIUS, width / 2 + RADIUS, height / 2 + RADIUS,
            90f + ANGLE / 2, 360f - ANGLE, false, paint
        )
        paint.pathEffect = effectPath

        canvas?.drawArc(
            width / 2 - RADIUS, height / 2 - RADIUS, width / 2 + RADIUS, height / 2 + RADIUS,
            90f + ANGLE / 2, 360f - ANGLE, false, paint
        )
        paint.pathEffect = null
        canvas?.drawLine(
            width / 2f,
            height / 2f,
            (cos(Math.toRadians(getAngleFromMark(110f))) * LINE_LENGTH).toFloat() + width / 2,
            (sin(Math.toRadians(getAngleFromMark(110f))) * LINE_LENGTH).toFloat() + height / 2,
            paint
        )
    }

    private fun getAngleFromMark(mark: Float): Double {
        return (90f + ANGLE / 2f + (360f - ANGLE) / 20f * mark).toDouble()
    }


    companion object {
        const val ANGLE = 120f
        val RADIUS = DisplayUtil.dp2px(150f)
        val LINE_LENGTH = DisplayUtil.dp2px(100f)
    }

}