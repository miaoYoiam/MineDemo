package com.example.minedemo.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.example.minedemo.App
import kotlinx.android.synthetic.main.item_widget_lucky_draw.view.*
import com.example.minedemo.R

/**
 * Day：4/26/21 2:25 PM
 * @author zhanglei
 *
 * @see R.layout.item_widget_lucky_draw
 */

class LuckyDrawWidget : FrameLayout {
    private val finallyLuckyPosition = 1
    private val startLuckyPosition = 0 //开始抽奖的位置
    private var childLuckyNum = 0 //奖品数量

    private var valueAnimator: ValueAnimator? = null
    private var listener: OnLuckSelectedEndListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, 0)

    fun setData() {
        val child0 = inflateChild().apply {
            setConfig(R.color.red, "x30", ContextCompat.getColor(App.context, R.color.bg_D7EEFF), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child1 = inflateChild().apply {
            setConfig(R.color.blue_6, "x1", ContextCompat.getColor(App.context, R.color.bg_EDDFFA), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child2 = inflateChild().apply {
            setConfig(R.color.red, "x1", ContextCompat.getColor(App.context, R.color.bg_D7EEFF), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child3 = inflateChild().apply {
            setConfig(R.color.blue_6, "x1", ContextCompat.getColor(App.context, R.color.bg_EDDFFA), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child4 = inflateChild().apply {
            setConfig(R.color.red, "x30", ContextCompat.getColor(App.context, R.color.bg_D7EEFF), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child5 = inflateChild().apply {
            setConfig(R.color.blue_6, "x1", ContextCompat.getColor(App.context, R.color.bg_EDDFFA), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child6 = inflateChild().apply {
            setConfig(R.color.red, "x100", ContextCompat.getColor(App.context, R.color.bg_D7EEFF), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child7 = inflateChild().apply {
            setConfig(R.color.blue_6, "x1", ContextCompat.getColor(App.context, R.color.bg_EDDFFA), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        val child8 = inflateChild().apply {
            setConfig(R.color.red, "x1", ContextCompat.getColor(App.context, R.color.bg_D7EEFF), ContextCompat.getColor(App.context, R.color.bg_FDD23D))
        }
        lucky_draw_container.addView(child0)
        lucky_draw_container.addView(child1)
        lucky_draw_container.addView(child2)
        lucky_draw_container.addView(child3)
        lucky_draw_container.addView(child4)
        lucky_draw_container.addView(child5)
        lucky_draw_container.addView(child6)
        lucky_draw_container.addView(child7)
        lucky_draw_container.addView(child8)
        childLuckyNum = lucky_draw_container.childCount
    }

    fun setOnLuckSelectedEndListener(listener: OnLuckSelectedEndListener) {
        this.listener = listener
    }

    private fun inflateChild(): LuckyDrawChildWidget {
        return LayoutInflater.from(context).inflate(R.layout.item_widget_lucky_draw_child, lucky_draw_container, false) as LuckyDrawChildWidget
    }

    fun startLuckDraw() {
        valueAnimator = ValueAnimator.ofInt(startLuckyPosition, (REPEAT_COUNT - 1) * childLuckyNum + finallyLuckyPosition).setDuration(3000)
        valueAnimator?.addUpdateListener {
            val position = it.animatedValue as Int
            refreshChildView(position % childLuckyNum)
        }
        valueAnimator?.interpolator = DecelerateInterpolator(1.5f)
        valueAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                listener?.onFinish(finallyLuckyPosition)

            }
        })
        valueAnimator?.start()
    }

    private fun refreshChildView(luckPosition: Int) {
        for (i in 0 until childLuckyNum) {
            val child = lucky_draw_container.getChildAt(i) as? LuckyDrawChildWidget
            child?.refresh(luckPosition == i)
        }
    }

    interface OnLuckSelectedEndListener {
        fun onFinish(luckPosition: Int)
    }

    private fun cleared() {

    }

    companion object {
        const val REPEAT_COUNT = 2
    }
}