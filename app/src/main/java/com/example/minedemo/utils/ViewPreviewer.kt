package com.example.minedemo.utils

import android.os.Looper

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.View
import java.lang.Exception
import java.util.concurrent.FutureTask


/**
 * Day：2021/12/15 4:37 下午
 * @author zhanglei
 */
object ViewPreviewer {
    private val TAG = "ViewPreviewer"

    private val mMainHandler: Handler = Handler(Looper.getMainLooper())

    fun createPreview(view: View?, width: Int, height: Int): Bitmap? {
        if (view == null) {
            return null
        }
        val task: FutureTask<Bitmap?> = FutureTask {
            Log.e(TAG, "FutureTask ${Thread.currentThread().name}")

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            // Draw clock view hierarchy to canvas.
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.TRANSPARENT)
//            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
//            view.layout(0, 0, width, height)
            view.draw(canvas)
            bitmap
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            task.run()
        } else {
            mMainHandler.post(task)
        }
        return try {
            task.get()
        } catch (e: Exception) {
            Log.e(TAG, "Error completing task", e)
            null
        }
    }




}