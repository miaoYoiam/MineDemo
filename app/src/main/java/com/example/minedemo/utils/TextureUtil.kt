package com.example.minedemo.utils

import android.graphics.*

object TextureUtil {
    @JvmStatic
    fun createTextImage(paint: Paint, text: String, bgColor: Int = Color.TRANSPARENT, padding: Int = 0): Bitmap? {
        val width = paint.measureText(text, 0, text.length)
        val top = paint.fontMetrics.top
        val bottom = paint.fontMetrics.bottom
        val bm = Bitmap.createBitmap((width + padding * 2).toInt(), (bottom - top + padding * 2).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)

        //清掉
        if (bgColor != Color.TRANSPARENT) {
            canvas.drawColor(bgColor)
        }
        paint.isDither = true
        paint.isFilterBitmap = true
        canvas.drawText(text, padding.toFloat(), -top + padding, paint)
        return bm
    }

    @JvmStatic
    fun clearBitmap(bitmap: Bitmap) {
        bitmap.eraseColor(Color.TRANSPARENT)
    }
}