package com.example.minedemo.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.example.minedemo.utils.dp

/**
 * Day：2022/6/9 9:26 下午
 * @author zhanglei
 */
class DashboardView(context: Context, attr: AttributeSet?) : View(context, attr) {

    private val paint = Paint()
    private val dash = Path()
    private val arcPath = Path()

    private var dashEffect: PathDashPathEffect? = null

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = 3.dp
        paint.style = Paint.Style.STROKE

        dash.reset()
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
        dashEffect = PathDashPathEffect(dash, 50f, 50f, PathDashPathEffect.Style.ROTATE)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        arcPath.reset()
        arcPath.addArc(
            width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS,
            90f + OPEN_ANGLE / 2f, 360f - OPEN_ANGLE
        )
        val pathMeasure = PathMeasure(arcPath, false)
        val advance = (pathMeasure.length - DASH_WIDTH) / 20f
        dashEffect = PathDashPathEffect(dash, advance, 0f, PathDashPathEffect.Style.ROTATE)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(arcPath, paint)

        paint.pathEffect = dashEffect
        canvas?.drawPath(arcPath, paint)
        paint.pathEffect = null

//        canvas?.drawLine(width / 2f, height / 2f,width/2f+ LENGTH*)
    }

    companion object {
        private const val OPEN_ANGLE = 120
        private val RADIUS = 150.dp
        private val LENGTH = 100.dp
        private val DASH_WIDTH = 2.dp
        private val DASH_LENGTH = 10.dp
    }
}