package com.example.minedemo.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.example.minedemo.R
import com.example.minedemo.utils.DisplayUtil

class SongLoadProgressWidget : View {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        0
    ) {
        val attributes = context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalLoadProgressBar,
            defStyleAttr,
            0
        )
        initByAttributes(attributes)
        attributes?.recycle()
        initView()
    }

    private var mWidth = 0f
    private var mHeight = 0f
    private var mRadius: Float? = 0f
    private var mProgress = 0f

    private lateinit var bgPaint: Paint
    private lateinit var bgRectF: RectF
    private lateinit var bgPath: Path
    private var bgColor: Int? = null

    private lateinit var progressPaint: Paint
    private lateinit var progressRectF: RectF
    private var progressColor: Int? = null

    private lateinit var textPaint: Paint
    private lateinit var fontMetrics: Paint.FontMetrics
    private var textProgress: String? = null
    private var mTextLabel: String? = null
    private var mTextSize: Float? = null
    private var mTextColor: Int? = null

    private fun initByAttributes(attributes: TypedArray?) {
        attributes?.let {
            bgColor = it.getColor(
                R.styleable.HorizontalLoadProgressBar_progress_bg_color,
                ContextCompat.getColor(context, R.color.white_alpha_10)
            )
            progressColor = it.getColor(
                R.styleable.HorizontalLoadProgressBar_progress_color,
                ContextCompat.getColor(context, R.color.blue_6)
            )
            mTextColor = it.getColor(
                R.styleable.HorizontalLoadProgressBar_progress_text_color,
                ContextCompat.getColor(context, R.color.blue_6)
            )
            mTextLabel = it.getString(R.styleable.HorizontalLoadProgressBar_progress_text_label)
            mTextSize = it.getDimension(
                R.styleable.HorizontalLoadProgressBar_progress_text_size,
                DisplayUtil.dp2px(17f).toFloat()
            )
            mRadius = it.getDimension(
                R.styleable.HorizontalLoadProgressBar_progress_radius,
                DisplayUtil.dp2px(7f).toFloat()
            )
        }
    }


    private fun initView() {
        mWidth = DisplayUtil.dp2px(335f)
        mHeight = DisplayUtil.dp2px(43f)

        bgPaint = Paint().apply {
            isAntiAlias = true
            color = bgColor ?: ContextCompat.getColor(context, R.color.white_alpha_10)
            style = Paint.Style.FILL
        }
        bgRectF = RectF(0f, 0f, mWidth, mHeight)
        bgPath = Path().apply {
            fillType = Path.FillType.EVEN_ODD
            addRoundRect(
                bgRectF, mRadius ?: DisplayUtil.dp2px(7f).toFloat(), mRadius
                    ?: DisplayUtil.dp2px(7f).toFloat(), Path.Direction.CW
            )
        }

        progressPaint = Paint().apply {
            isAntiAlias = true
            color = progressColor ?: ContextCompat.getColor(context, R.color.blue_6)
        }
        progressRectF = RectF(0f, 0f, 0f, 0f)

        textPaint = Paint().apply {
            isAntiAlias = true
            color = mTextColor ?: ContextCompat.getColor(context, R.color.white)
            textSize = mTextSize ?: DisplayUtil.dp2px(17f).toFloat()
            textAlign = Paint.Align.CENTER
        }
        fontMetrics = textPaint.fontMetrics
    }

    fun setProgress(progress: Float) {
        if (mProgress >= MAX_PROGRESS) {
            return
        }
        mProgress = when {
            progress < 0f -> {
                0f
            }
            progress > MAX_PROGRESS -> {
                MAX_PROGRESS
            }
            else -> {
                progress
            }
        }
        textProgress = "$mProgress%"
        val mLeft = -mWidth + mWidth * (mProgress / MAX_PROGRESS)
        val mRight = mWidth * (mProgress / MAX_PROGRESS)
        progressRectF.set(mLeft, 0f, mRight, mHeight)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = resolveSize(mWidth.toInt(), widthMeasureSpec).toFloat()
        mHeight = resolveSize(mHeight.toInt(), heightMeasureSpec).toFloat()
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(bgPath, bgPaint)
        canvas?.clipPath(bgPath)

        canvas?.drawRoundRect(
            progressRectF, mRadius ?: DisplayUtil.dp2px(7f).toFloat(), mRadius
                ?: DisplayUtil.dp2px(7f).toFloat(), progressPaint
        )

        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseline: Float = bgRectF.centerY() + distance
        canvas?.drawText(textProgress + mTextLabel, bgRectF.centerX(), baseline, textPaint)

    }

    companion object {
        const val MAX_PROGRESS = 100f
    }
}