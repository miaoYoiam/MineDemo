package com.example.minedemo.widget

import android.view.animation.DecelerateInterpolator

/**
 * Day：4/26/21 4:12 PM
 * @author zhanglei
 */
class StageDecelerateInterpolator(val factor: Float) : DecelerateInterpolator() {
    override fun getInterpolation(input: Float): Float {
        val result = if (input < factor) {
            (1.0f - (1.0f - input) * (1.0f - input))
        } else {
            println("<<<<< $input    ${(1.0f - (1.0f - input) * (1.0f - input))}")
            (1.0f - (1.0f - input) * (1.0f - input))
        }
        println("-----> $result")
        return result
    }
}