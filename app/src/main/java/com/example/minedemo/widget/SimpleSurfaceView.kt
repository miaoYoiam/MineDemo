package com.example.minedemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi

/**
 * Day：9/10/21 6:12 PM
 * @author zhanglei
 */
class SimpleSurfaceView : SurfaceView, Runnable, SurfaceHolder.Callback {

    private var mHolder: SurfaceHolder? = null

    private var mThread: Thread? = null

    @Volatile
    private var flag: Boolean = false

    private var mCanvas: Canvas? = null

    private var mPaint: Paint? = null

    var m_circle_r = 10f


    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, 0) {

        mHolder = holder
        mHolder?.addCallback(this)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = Color.WHITE
        mPaint?.strokeWidth = 10f
        mPaint?.style = Paint.Style.FILL


        focusable = FOCUSABLE
    }

    override fun run() {
        while (flag) {
            try {
                mHolder?.let {
                    synchronized(it) {
                        Thread.sleep(100)

                        drawCircle()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {

            }
        }
    }

    private fun drawCircle() {
        mCanvas = mHolder?.lockCanvas()
        mCanvas?.let { canvas ->

            if (m_circle_r >= (width / 10)) {
                m_circle_r = 0f
            } else {
                m_circle_r++
            }

            for (i in 0 until 5) {
                for (j in 0 until 8) {
                    mPaint?.let { canvas.drawCircle((width / 5f) * i + (width / 10f), (height / 8f) * j + height / 16f, m_circle_r, it) }
                }
            }

            mHolder?.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        flag = true
        mThread = Thread(this)
        mThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        flag = false
        mHolder?.removeCallback(this)
    }
}