package com.example.minedemo.utils

import android.content.res.Resources
import android.util.TypedValue

class DisplayUtil {

    companion object {
        fun dp2px(dp: Float): Float {
            if (dp < 0) {
                return 0f
            }
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().displayMetrics
            )
        }
    }
}