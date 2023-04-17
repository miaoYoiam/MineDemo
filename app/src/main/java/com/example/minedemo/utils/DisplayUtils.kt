package com.example.minedemo.utils

import android.content.Context
import android.content.res.Resources
import android.view.View

fun dp2px(context: Context, dp: Int): Float {
    val scale = context.resources.displayMetrics.density
    return dp * scale + 0.5f
}

fun dp2px(context: Context, dp: Float): Float {
    val scale = context.resources.displayMetrics.density
    return dp * scale + 0.5f
}

val Int.dp: Float
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return this * scale + 0.5f
    }

val Float.dp: Float
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return this * scale + 0.5f
    }

val Double.dp: Float
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5).toFloat()
    }

val Int.dpi: Int
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

val Float.dpi: Int
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

val Double.dpi: Int
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5).toInt()
    }

inline fun View.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()


