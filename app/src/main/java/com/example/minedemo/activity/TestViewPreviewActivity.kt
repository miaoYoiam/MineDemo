package com.example.minedemo.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.example.minedemo.R
import kotlinx.android.synthetic.main.activity_test_view_preview.*

class TestViewPreviewActivity : AppCompatActivity() {
    private var timber = 0

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1001 -> {
                    timber++
                    timer_text.text = timber.toString()

                    sendEmptyMessageDelayed(1001, 1000)
                }
                1002 -> {
                    setPreview()

                    sendEmptyMessageDelayed(1002, 500)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_view_preview)

        timer_text.animation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            val lin = LinearInterpolator()
            interpolator = lin
            duration = 2500
            repeatCount = -1
            fillAfter = true
        }
        startTimer()
        startPreview()
    }


    private fun startTimer() {
        handler.sendEmptyMessageDelayed(1001, 1000)
    }

    private fun startPreview() {
        handler.sendEmptyMessageDelayed(1002, 500)
    }

    private fun setPreview() {
        val bitmap = createPreview(timer_container, timer_container.measuredWidth, timer_container.measuredHeight)

        bufferBitmap?.recycle()
        bufferBitmap = null
        bufferBitmap = bitmap?.copy(Bitmap.Config.ARGB_8888, true)

        view_previewer.setImageBitmap(bufferBitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private var bufferBitmap: Bitmap? = null
    private var previewBitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null

    fun createPreview(view: View?, width: Int, height: Int): Bitmap? {
        if (view == null) {
            return null
        }

        if (previewBitmap == null) {
            previewBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                previewBitmap?.isPremultiplied = true
            }
            previewBitmap?.setHasAlpha(true)
            previewBitmap?.eraseColor(Color.TRANSPARENT)
        }

        if (bitmapCanvas == null) {
            bitmapCanvas = previewBitmap?.let { Canvas(it) }
        } else {
            bitmapCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        }

        view.draw(bitmapCanvas)

        return previewBitmap
    }


}