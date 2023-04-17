package com.example.minedemo.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import com.example.minedemo.R

/**
 * Day：2022/8/3 2:57 下午
 * @author zhanglei
 */
object WealthLevelDrawableHelper {
    private var level0: Bitmap? = null
    private var level1: Bitmap? = null
    private var level2: Bitmap? = null
    private var level3: Bitmap? = null
    private var level4: Bitmap? = null
    private var level5: Bitmap? = null
    private var level6: Bitmap? = null
    private var level7: Bitmap? = null
    private var level8: Bitmap? = null

    fun getLevelBitmap(context: Context, level: Int): Bitmap? {
        return when (level) {
            in 5 until 10 -> {
                if (level1 == null) {
                    level1 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_1)
                }
                level1
            }
            in 10 until 15 -> {
                if (level2 == null) {
                    level2 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_2)
                }
                level2
            }
            in 15 until 20 -> {
                if (level3 == null) {
                    level3 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_3)
                }
                level3
            }
            in 20 until 25 -> {
                if (level4 == null) {
                    level4 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_4)
                }
                level4
            }
            in 25 until 30 -> {
                if (level5 == null) {
                    level5 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_5)
                }
                level5
            }
            in 30 until 35 -> {
                if (level6 == null) {
                    level6 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_6)
                }
                level6
            }
            in 35 until 36 -> {
                if (level7 == null) {
                    level7 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_7)
                }
                level7
            }
            in 36 until Int.MAX_VALUE -> {
                if (level8 == null) {
                    level8 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_8)
                }
                level8
            }
            else -> {
                if (level0 == null) {
                    level0 = BitmapFactory.decodeResource(context.resources, R.drawable.icon_wealth_level_0)
                }
                level0
            }
        }
    }

    /**
     * WealthLevelDrawableV1
     */

    private var textColor0: Int? = null
    private var textColor1: Int? = null
    private var textColor2: Int? = null
    private var textColor3: Int? = null
    private var textColor4: Int? = null
    private var textColor5: Int? = null
    private var textColor6: Int? = null
    private var textColor7: Int? = null
    private var textColor8: Int? = null

    fun getTextColorForV1(context: Context, level: Int): Int? {
        return when (level) {
            in 5 until 10 -> {
                if (textColor1 == null) {
                    textColor1 = ContextCompat.getColor(context, R.color.bg_673218)
                }
                textColor1
            }
            in 10 until 15 -> {
                if (textColor2 == null) {
                    textColor2 = ContextCompat.getColor(context, R.color.bg_094556)
                }
                textColor2
            }
            in 15 until 20 -> {
                if (textColor3 == null) {
                    textColor3 = ContextCompat.getColor(context, R.color.bg_174417)
                }
                textColor3
            }
            in 20 until 25 -> {
                if (textColor4 == null) {
                    textColor4 = ContextCompat.getColor(context, R.color.bg_04518F)
                }
                textColor4
            }
            in 25 until 30 -> {
                if (textColor5 == null) {
                    textColor5 = ContextCompat.getColor(context, R.color.bg_9F1E5B)
                }
                textColor5
            }
            in 30 until 35 -> {
                if (textColor6 == null) {
                    textColor6 = ContextCompat.getColor(context, R.color.bg_A85100)
                }
                textColor6
            }
            in 35 until 36 -> {
                if (textColor7 == null) {
                    textColor7 = ContextCompat.getColor(context, R.color.bg_A85100)
                }
                textColor7
            }
            in 36 until Int.MAX_VALUE -> {
                if (textColor8 == null) {
                    textColor8 = ContextCompat.getColor(context, R.color.bg_5900BC)
                }
                textColor8
            }
            else -> {
                if (textColor0 == null) {
                    textColor0 = ContextCompat.getColor(context, R.color.bg_4B4B4B)
                }
                textColor0
            }
        }
    }

    fun getBitmapScaleSizeForV1(size: Float): Float {
        return size * 4f / 94f
    }

    fun getTextSizeForV1(size: Float): Float {
        return size * 46f / 94f
    }

    fun getStrokeSizeForV1(size: Float): Float {
        return size * 3f / 94f
    }

    /**
     * WealthLevelDrawableV2
     */

    fun getTextSizeForV2(size: Float): Float {
        return size * 8f / 14f
    }

    fun getTextRightBoundForV2(size: Float): Float {
        return size * 3f / 14f
    }

    fun getRectLeftBoundForV2(size: Float): Float {
        return size * 8f / 14f
    }

    fun getRectHeightForV2(size: Float): Float {
        return size * 11f / 14f
    }

    fun getRectWidthForV2(size: Float, lv: Int): Float {
        return if (lv < 10) {
            size * 17f / 14f
        } else {
            size * 21f / 14f
        }
    }

    fun getRectStrokeSizeForV2(size: Float): Float {
        return size * 0.5f / 14f
    }

    fun getRectRoundForV2(size: Float): Float {
        return size * 2f / 14f
    }

    private var colorArray0: IntArray? = null
    private var colorArray1: IntArray? = null
    private var colorArray2: IntArray? = null
    private var colorArray3: IntArray? = null
    private var colorArray4: IntArray? = null
    private var colorArray5: IntArray? = null
    private var colorArray6: IntArray? = null
    private var colorArray7: IntArray? = null
    private var colorArray8: IntArray? = null

    fun getLinearGradientColorArrayForV2(context: Context, level: Int): IntArray? {
        return when (level) {
            in 5 until 10 -> {
                if (colorArray1 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_994319)
                    val endColor = ContextCompat.getColor(context, R.color.bg_BE9D8E)
                    colorArray1 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray1
            }
            in 10 until 15 -> {
                if (colorArray2 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_10586B)
                    val endColor = ContextCompat.getColor(context, R.color.bg_5596AD)
                    colorArray2 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray2
            }
            in 15 until 20 -> {
                if (colorArray3 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_164727)
                    val endColor = ContextCompat.getColor(context, R.color.bg_5596AD)
                    colorArray3 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray3
            }
            in 20 until 25 -> {
                if (colorArray4 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_004BAB)
                    val endColor = ContextCompat.getColor(context, R.color.bg_27A0C7)
                    colorArray4 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray4
            }
            in 25 until 30 -> {
                if (colorArray5 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_A81F62)
                    val endColor = ContextCompat.getColor(context, R.color.bg_CF6394)
                    colorArray5 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray5
            }
            in 30 until 35 -> {
                if (colorArray6 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_904400)
                    val endColor = ContextCompat.getColor(context, R.color.bg_E79922)
                    colorArray6 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray6
            }
            in 35 until 36 -> {
                if (colorArray7 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_904400)
                    val endColor = ContextCompat.getColor(context, R.color.bg_E79922)
                    colorArray7 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray7
            }
            in 36 until Int.MAX_VALUE -> {
                if (colorArray8 == null) {
                    val startColor = ContextCompat.getColor(context, R.color.bg_210063)
                    val endColor = ContextCompat.getColor(context, R.color.bg_931FFA)
                    colorArray8 = IntArray(2).apply {
                        set(0, startColor)
                        set(1, endColor)
                    }
                }
                colorArray8
            }
            else -> {
                if (colorArray0 == null) {
                    val color = ContextCompat.getColor(context, R.color.bg_7F7F7F)
                    colorArray0 = IntArray(2).apply {
                        set(0, color)
                        set(1, color)
                    }
                }
                colorArray0
            }
        }
    }

    private var strokeColor0: Int? = null
    private var strokeColor1: Int? = null
    private var strokeColor2: Int? = null
    private var strokeColor3: Int? = null
    private var strokeColor4: Int? = null

    fun getRectStrokeColorForV2(context: Context, level: Int): Int? {
        return when (level) {
            in 20 until 25 -> {
                if (strokeColor0 == null) {
                    strokeColor0 = ContextCompat.getColor(context, R.color.bg_D2FFFC)
                }
                strokeColor0
            }
            in 25 until 30 -> {
                if (strokeColor1 == null) {
                    strokeColor1 = ContextCompat.getColor(context, R.color.bg_FFBB79)
                }
                strokeColor1
            }
            in 30 until 35 -> {
                if (strokeColor2 == null) {
                    strokeColor2 = ContextCompat.getColor(context, R.color.bg_FFEAA6)
                }
                strokeColor2
            }
            in 35 until 36 -> {
                if (strokeColor3 == null) {
                    strokeColor3 = ContextCompat.getColor(context, R.color.bg_FFEAA6)
                }
                strokeColor3
            }
            in 36 until Int.MAX_VALUE -> {
                if (strokeColor4 == null) {
                    strokeColor4 = ContextCompat.getColor(context, R.color.bg_FFE07C)
                }
                strokeColor4
            }
            else -> {
                null
            }
        }
    }

}