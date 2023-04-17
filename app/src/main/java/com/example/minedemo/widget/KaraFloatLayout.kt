/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, setRadius Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain setRadius copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.minedemo.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.example.minedemo.R

/**
 * 流式布局
 *
 * 在 xml 中采用 控制以上属性。
 */
class KaraFloatLayout : ViewGroup {
    private var mChildHorizontalSpacing = 0
    private var mChildVerticalSpacing = 0
    private var mGravity = 0
    private var mMaxMode = LINES
    private var mMaximum = Int.MAX_VALUE

    private var mLineCount = 0

    private var mOnLineCountChangeListener: OnLineCountChangeListener? = null

    /**
     * <p>每一行的item数目，下标表示行下标，在onMeasured的时候计算得出，供onLayout去使用。</p>
     * <p>若mItemNumberInEachLine[*]==0，则表示第x行已经没有item了</p>
     */
    private var mItemNumberInEachLine: IntArray = intArrayOf()

    /**
     * <p>每一行的item的宽度和（包括item直接的间距），下标表示行下标，
     * 如 mWidthSumInEachLine[*]表示第x行的item的宽度和（包括item直接的间距）</p>
     * <p>在onMeasured的时候计算得出，供onLayout去使用</p>
     */
    private var mWidthSumInEachLine: IntArray = intArrayOf()

    // onMeasure过程中实际参与measure的子View个数
    private var measuredChildCount = 0

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attributeSet: AttributeSet?) : this(ctx, attributeSet, 0)
    constructor(ctx: Context, attributeSet: AttributeSet?, defStyleAtrr: Int) : super(ctx, attributeSet, defStyleAtrr) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.KaraFloatLayout)
        mChildHorizontalSpacing = array.getDimensionPixelSize(R.styleable.KaraFloatLayout_childHorizontalSpacing, 0)
        mChildVerticalSpacing = array.getDimensionPixelSize(R.styleable.KaraFloatLayout_childVerticalSpacing, 0)
        mGravity = array.getInteger(R.styleable.KaraFloatLayout_android_gravity, Gravity.START)
        val maxLines = array.getInt(R.styleable.KaraFloatLayout_android_maxLines, -1)
        if (maxLines >= 0) {
            setMaxLines(maxLines)
        }
        val maxNumber = array.getInt(R.styleable.KaraFloatLayout_maxNumber, -1)
        if (maxNumber >= 0) {
            setMaxNumber(maxNumber)
        }
        array.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        var maxLineHeight = 0

        var resultWidth: Int
        var resultHeight: Int

        val count = childCount

        mItemNumberInEachLine = IntArray(count)
        mWidthSumInEachLine = IntArray(count)
        var lineIndex = 0

        // 若FloatLayout指定了MATCH_PARENT或固定宽度，则需要使子View换行
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            resultWidth = widthSpecSize
            measuredChildCount = 0

            // 下一个子View的position
            var childPositionX = paddingLeft
            var childPositionY = paddingTop

            // 子View的Right最大可达到的x坐标
            val childMaxRight = widthSpecSize - paddingRight

            for (i in 0 until count) {
                if (mMaxMode == NUMBER && measuredChildCount >= mMaximum) {
                    // 超出最多数量，则不再继续
                    break
                } else if (mMaxMode == LINES && lineIndex >= mMaximum) {
                    // 超出最多行数，则不再继续
                    break
                }

                val child = getChildAt(i)
                if (child.visibility == View.GONE) {
                    continue
                }

                val childLayoutParams = child.layoutParams
                val childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLayoutParams.width)
                val childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLayoutParams.height)
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)

                val childWidth = child.measuredWidth
                maxLineHeight = Math.max(maxLineHeight, child.measuredHeight)
                // 需要换行
                if (childPositionX + childWidth > childMaxRight) {
                    // 如果换行后超出最大行数，则不再继续
                    if (mMaxMode == LINES) {
                        if (lineIndex + 1 >= mMaximum) {
                            break
                        }
                    }
                    mWidthSumInEachLine[lineIndex] -= mChildHorizontalSpacing // 后面每次加item都会加上一个space，这样的话每行都会为最后一个item多加一次space，所以在这里减一次
                    lineIndex++ // 换行
                    childPositionX = paddingLeft // 下一行第一个item的x
                    childPositionY += maxLineHeight + mChildVerticalSpacing // 下一行第一个item的y
                }
                mItemNumberInEachLine[lineIndex]++
                mWidthSumInEachLine[lineIndex] += childWidth + mChildHorizontalSpacing
                childPositionX += childWidth + mChildHorizontalSpacing
                measuredChildCount++
            }
            // 如果最后一个item不是刚好在行末（即lineCount最后没有+1，也就是mWidthSumInEachLine[lineCount]非0），则要减去最后一个item的space
            if (mWidthSumInEachLine.isNotEmpty() && mWidthSumInEachLine[lineIndex] > 0) {
                mWidthSumInEachLine[lineIndex] -= mChildHorizontalSpacing
            }

            when (heightSpecMode) {
                MeasureSpec.UNSPECIFIED -> {
                    resultHeight = childPositionY + maxLineHeight + paddingBottom
                }
                MeasureSpec.AT_MOST -> {
                    resultHeight = childPositionY + maxLineHeight + paddingBottom
                    resultHeight = Math.min(resultHeight, heightSpecSize)
                }
                else -> {
                    resultHeight = heightSpecSize
                }
            }
        } else {
            // 不计算换行，直接一行铺开
            resultWidth = paddingLeft + paddingRight
            measuredChildCount = 0

            for (i in 0 until count) {
                if (mMaxMode == NUMBER) {
                    // 超出最多数量，则不再继续
                    if (measuredChildCount > mMaximum) {
                        break
                    }
                } else if (mMaxMode == LINES) {
                    // 超出最大行数，则不再继续
                    if (1 > mMaximum) {
                        break
                    }
                }
                val child = getChildAt(i)
                if (child.visibility == View.GONE) {
                    continue
                }
                val childLayoutParams = child.layoutParams
                val childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLayoutParams.width)
                val childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLayoutParams.height)
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
                resultWidth += child.measuredWidth
                maxLineHeight = Math.max(maxLineHeight, child.measuredHeight)
                measuredChildCount++
            }
            if (measuredChildCount > 0) {
                resultWidth += mChildHorizontalSpacing * (measuredChildCount - 1)
            }
            resultHeight = maxLineHeight + paddingTop + paddingBottom
            if (mItemNumberInEachLine.isNotEmpty()) {
                mItemNumberInEachLine[lineIndex] = count
            }
            if (mWidthSumInEachLine.isNotEmpty()) {
                mWidthSumInEachLine[0] = resultWidth
            }
        }
        setMeasuredDimension(resultWidth, resultHeight)
        val measureLineCount = lineIndex + 1
        if (mLineCount != measureLineCount) {
            mOnLineCountChangeListener?.onChange(mLineCount, measureLineCount)
            mLineCount = measureLineCount
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        when (mGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.START -> layoutWithGravityLeft(width)
            Gravity.END -> layoutWithGravityRight(width)
            Gravity.CENTER_HORIZONTAL -> layoutWithGravityCenterHorizontal(width)
            else -> layoutWithGravityLeft(width)
        }
    }

    /**
     * 将子View居中布局
     */
    private fun layoutWithGravityCenterHorizontal(parentWidth: Int) {
        var nextChildIndex = 0
        var nextChildPositionX: Int
        var nextChildPositionY = paddingTop
        var lineHeight = 0
        var layoutChildCount = 0
        var layoutChildEachLine = 0

        // 遍历每一行
        for (i in mItemNumberInEachLine.indices) {
            // 如果这一行已经没item了，则退出循环
            if (mItemNumberInEachLine[i] == 0) {
                break
            }

            // 遍历该行内的元素，布局每个元素
            nextChildPositionX = (parentWidth - paddingLeft - paddingRight - mWidthSumInEachLine[i]) / 2 + paddingLeft // 子 View 的最小 x 值
            while (layoutChildEachLine < mItemNumberInEachLine[i]) {
                val childView = getChildAt(nextChildIndex)
                if (childView.visibility == View.GONE) {
                    nextChildIndex++
                    continue
                }
                val childWidth = childView.measuredWidth
                val childHeight = childView.measuredHeight
                childView.layout(nextChildPositionX, nextChildPositionY, nextChildPositionX + childWidth, nextChildPositionY + childHeight)
                lineHeight = Math.max(lineHeight, childHeight)
                nextChildPositionX += childWidth + mChildHorizontalSpacing
                layoutChildCount++
                layoutChildEachLine++
                nextChildIndex++
                if (layoutChildCount == measuredChildCount) {
                    break
                }
            }
            if (layoutChildCount == measuredChildCount) {
                break
            }

            // 一行结束了，整理一下，准备下一行
            nextChildPositionY += lineHeight + mChildVerticalSpacing
            lineHeight = 0
            layoutChildEachLine = 0
        }
        val childCount = childCount
        for (i in nextChildIndex until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility == View.GONE) {
                continue
            }
            childView.layout(0, 0, 0, 0)
        }
    }

    /**
     * 将子View靠左布局
     */
    private fun layoutWithGravityLeft(parentWidth: Int) {
        val childMaxRight = parentWidth - paddingRight
        var childPositionX = paddingLeft
        var childPositionY = paddingTop
        var lineHeight = 0
        val childCount = childCount
        var layoutChildCount = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            if (layoutChildCount < measuredChildCount) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                if (childPositionX + childWidth > childMaxRight) {
                    // 换行
                    childPositionX = paddingLeft
                    childPositionY += lineHeight + mChildVerticalSpacing
                    lineHeight = 0
                }
                child.layout(childPositionX, childPositionY, childPositionX + childWidth, childPositionY + childHeight)
                childPositionX += childWidth + mChildHorizontalSpacing
                lineHeight = Math.max(lineHeight, childHeight)
                layoutChildCount++
            } else {
                child.layout(0, 0, 0, 0)
            }
        }
    }

    /**
     * 将子View靠右布局
     */
    private fun layoutWithGravityRight(parentWidth: Int) {
        var nextChildIndex = 0
        var nextChildPositionX: Int
        var nextChildPositionY = paddingTop
        var lineHeight = 0
        var layoutChildCount = 0
        var layoutChildEachLine = 0

        // 遍历每一行
        for (i in mItemNumberInEachLine.indices) {
            // 如果这一行已经没item了，则退出循环
            if (mItemNumberInEachLine[i] == 0) {
                break
            }

            // 遍历该行内的元素，布局每个元素
            nextChildPositionX = parentWidth - paddingRight - mWidthSumInEachLine[i] // 初始值为子 View 的最小 x 值
            while (layoutChildEachLine < mItemNumberInEachLine[i]) {
                val childView = getChildAt(nextChildIndex)
                if (childView.visibility == View.GONE) {
                    nextChildIndex++
                    continue
                }
                val childWidth = childView.measuredWidth
                val childHeight = childView.measuredHeight
                childView.layout(nextChildPositionX, nextChildPositionY, nextChildPositionX + childWidth, nextChildPositionY + childHeight)
                lineHeight = Math.max(lineHeight, childHeight)
                nextChildPositionX += childWidth + mChildHorizontalSpacing
                layoutChildCount++
                layoutChildEachLine++
                nextChildIndex++
                if (layoutChildCount == measuredChildCount) {
                    break
                }
            }
            if (layoutChildCount == measuredChildCount) {
                break
            }

            // 一行结束了，整理一下，准备下一行
            nextChildPositionY += lineHeight + mChildVerticalSpacing
            lineHeight = 0
            layoutChildEachLine = 0
        }

        val childCount = childCount
        for (i in nextChildIndex until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility == View.GONE) {
                continue
            }
            childView.layout(0, 0, 0, 0)
        }
    }

    fun setOnLineCountChangeListener(onLineCountChangeListener: OnLineCountChangeListener?) {
        mOnLineCountChangeListener = onLineCountChangeListener
    }

    /**
     * 设置子 View 的对齐方式，目前支持 [Gravity.CENTER_HORIZONTAL], [Gravity.LEFT] 和 [Gravity.RIGHT]
     */
    fun setGravity(gravity: Int) {
        if (mGravity != gravity) {
            mGravity = gravity
            requestLayout()
        }
    }

    fun getGravity(): Int {
        return mGravity
    }

    /**
     * 设置子 View 的水平间距
     */
    fun setChildHorizontalSpacing(spacing: Int) {
        mChildHorizontalSpacing = spacing
        invalidate()
    }

    /**
     * 设置子 View 的垂直间距
     */
    fun setChildVerticalSpacing(spacing: Int) {
        mChildVerticalSpacing = spacing
        invalidate()
    }

    /**
     * 设置最多可显示的行数
     * 注意该方法不会改变子View的个数，只会影响显示出来的子View个数
     *
     * @param maxLines 最多可显示的行数
     */
    fun setMaxLines(maxLines: Int) {
        mMaximum = maxLines
        mMaxMode = LINES
        requestLayout()
    }

    fun getLineCount(): Int {
        return mLineCount
    }

    /**
     * 设置最多可显示的子View个数
     * 该方法不会改变子View的个数，只会影响显示出来的子View个数
     *
     * @param maxNumber 最多可显示的子View个数
     */
    fun setMaxNumber(maxNumber: Int) {
        mMaximum = maxNumber
        mMaxMode = NUMBER
        requestLayout()
    }

    /**
     * 获取最多可显示的子View个数
     */
    fun getMaxNumber(): Int {
        return if (mMaxMode == NUMBER) mMaximum else -1
    }

    interface OnLineCountChangeListener {
        fun onChange(oldLineCount: Int, newLineCount: Int)
    }

    companion object {
        private const val LINES = 0
        private const val NUMBER = 1
    }
}