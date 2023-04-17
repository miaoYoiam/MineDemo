package com.example.minedemo.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.example.minedemo.R
import com.example.minedemo.utils.dp
import com.example.minedemo.utils.sp

/**
 * 新版唱歌得分显示控件
 * 得分的百分比以及对应得分的文字都是使用canvas绘制
 */
class SingScoreProgressViewV2 : FrameLayout {
    private val mPaint = Paint()
    private val mWidth = 278.dp
    private val mHeight = 34.dp
    private var currentIndex = -1

    private val bgColor by lazy {
        ContextCompat.getColor(context, R.color.black_alpha_60)
    }

    private val normalSize by lazy {
        sp(14)
    }

    private val boldSize by lazy {
        sp(24)
    }

    private val scoreSectionArray by lazy {
        arrayOf(
            ScoreSection("D", 7, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_3DE554)),
            ScoreSection("C", 21, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_32FBFF)),
            ScoreSection("B", 34, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_1688FF)),
            ScoreSection("A", 49, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_D12BFB)),
            ScoreSection("S", 63, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_FF370C)),
            ScoreSection("SS", 77, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_FF7200)),
            ScoreSection("SSS", 90, normalSize, boldSize, ContextCompat.getColor(context.applicationContext, R.color.bg_FFE23C))
        )

    }
    private val linearGradient by lazy {
        LinearGradient(
            0f,
            0F,
            mWidth,
            mHeight,
            intArrayOf(
                scoreSectionArray[0].scoreColor,
                scoreSectionArray[1].scoreColor,
                scoreSectionArray[2].scoreColor,
                scoreSectionArray[3].scoreColor,
                scoreSectionArray[4].scoreColor,
                scoreSectionArray[5].scoreColor,
                scoreSectionArray[6].scoreColor,
            ),
            null,
            Shader.TileMode.CLAMP
        )
    }
    private val scoreRectF by lazy {
        RectF()
    }

    private var scorePercent = 0
    private var scaleNormalSize = 0f
    private var scaleBoldSize = 0f


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        initPaint()
    }

    private fun initPaint() {
        mPaint.isAntiAlias = true
    }

    /**
     * 设置得分0-100
     */
    fun setScore(scorePercent: Int) {
        this.scorePercent = when {
            scorePercent < 0 -> 0
            scorePercent > 100 -> 100
            else -> scorePercent
        }


        var calculateIndex = -1
        var startPercent = 0
        var endPercent = 0
        when {
            scorePercent in 92..100 -> {
                calculateIndex = 6
                startPercent = 82
                endPercent = 100
            }
            scorePercent in 82..91 -> {
                calculateIndex = 5
                startPercent = 72
                endPercent = 82
            }
            scorePercent in 72..81 -> {
                calculateIndex = 4
                startPercent = 62
                endPercent = 72
            }
            scorePercent in 62..71 -> {
                calculateIndex = 3
                startPercent = 52
                endPercent = 62
            }
            scorePercent in 52..61 -> {
                calculateIndex = 2
                startPercent = 32
                endPercent = 52
            }
            scorePercent in 32..51 -> {
                calculateIndex = 1
                startPercent = 12
                endPercent = 32
            }
            scorePercent in 12..31 -> {
                calculateIndex = 0
                startPercent = 0
                endPercent = 12
            }
            scorePercent < 12 -> {
                calculateIndex = -1
            }
        }

        if (calculateIndex > currentIndex) {
            startAnimator(startPercent, endPercent, calculateIndex)
        }
    }

    /**
     * 动画属性
     */
    private fun setPercent(percent: Int) {
        this.scorePercent = percent
        invalidate()
    }

    private fun setScaleNormal(scale: Float) {
        this.scaleNormalSize = scale
        invalidate()
    }


    private fun setScaleBold(scale: Float) {
        this.scaleBoldSize = scale
        invalidate()
    }

    private val animationSet = AnimatorSet()
    private var percentAnimation: Animator? = null
    private var outerAnimation: Animator? = null
    private var scaleAnimation1: Animator? = null

    private var springAnimator: SpringAnimation? = null

    private fun startAnimator(startPercent: Int, endPercent: Int, endIndex: Int) {
        percentAnimation = ObjectAnimator.ofInt(this, "percent", startPercent, endPercent)
        percentAnimation?.duration = 500
        percentAnimation?.interpolator = AccelerateDecelerateInterpolator()

//        if (scaleAnimation0 == null) {
//            scaleAnimation0 = ObjectAnimator.ofFloat(this, "scaleNormal", boldSize.toFloat(), normalSize.toFloat())
//            scaleAnimation0?.duration = 300
//        }
//        scaleAnimation0?.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator?) {
//                currentIndex = endIndex
//            }
//
//            override fun onAnimationEnd(animation: Animator?) {
//            }
//
//            override fun onAnimationCancel(animation: Animator?) {
//            }
//
//            override fun onAnimationRepeat(animation: Animator?) {
//            }
//        })

        if (scaleAnimation1 == null) {
            scaleAnimation1 = ObjectAnimator.ofFloat(this, "scaleBold", normalSize.toFloat(), boldSize.toFloat() * 2.0f, normalSize.toFloat())
            scaleAnimation1?.duration = 1000
            scaleAnimation1?.interpolator = OvershootInterpolator(0.8f)
        }
        scaleAnimation1?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                currentIndex = endIndex
            }

            override fun onAnimationEnd(animation: Animator) {
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })

        if (outerAnimation == null) {
            outerAnimation = ObjectAnimator.ofFloat(this, "translationY", 0f, -top.toFloat() - measuredHeight)
            outerAnimation?.startDelay = 2000
            outerAnimation?.duration = 300
        }

        animationSet.cancel()
        animationSet.play(percentAnimation).with(scaleAnimation1).before(outerAnimation)
        animationSet.addListener(
            object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {

                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })

        val springForce = SpringForce(0f).apply {
            dampingRatio = 0.35f
            stiffness = 400f
        }
        springAnimator = SpringAnimation(this, SpringAnimation.TRANSLATION_Y).setSpring(springForce)
        springAnimator?.cancel()
        springAnimator?.setStartValue(-top.toFloat() - measuredHeight)
        springAnimator?.start()
        springAnimator?.addEndListener { animation, canceled, value, velocity ->
            animationSet.start()
        }

//        if (enterAnimation == null) {
//            enterAnimation = ObjectAnimator.ofFloat(this, "translationY", -top.toFloat() - measuredHeight, 0f)
//            enterAnimation?.duration = 300
//        }
//

    }

    /**
     * @param score 取值范围0-1
     */
    @SuppressLint("SetTextI18n")
    fun setScore(score: Double?) {
        score ?: return
        val iScore = (score * 100).toInt()
        setScore(iScore)
    }

    fun setTopScore(score: Double?) {
        if (score == null) return

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawBackground(this)
            drawGotScoreRect(this)
            drawScoreText(this)
        }
        super.onDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL
        mPaint.color = bgColor
        canvas.drawRoundRect(0f, 0f, mWidth, mHeight, 16.dp, 16.dp, mPaint)
    }

    /**
     * 绘制得到的分数区域
     */
    private fun drawGotScoreRect(canvas: Canvas) {
        val count = canvas.save()
        val endX = (mWidth * scorePercent.toFloat() / 100).toInt()
        scoreRectF.set(0f, 0f, endX.toFloat(), mHeight)
        mPaint.style = Paint.Style.FILL
        mPaint.shader = linearGradient
        if (scorePercent != 100) {
            canvas.clipRect(0f, 0f, scoreRectF.right - 16.dp, mHeight, Region.Op.INTERSECT)
        }
        canvas.drawRoundRect(scoreRectF, 16.dp, 16.dp, mPaint)
        canvas.restoreToCount(count)
    }

    private fun drawScoreText(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 0F
        mPaint.shader = null
        mPaint.color = Color.WHITE
        repeat(scoreSectionArray.size) {
            val scoreSection = scoreSectionArray[it]
            when (it) {
                currentIndex -> {
                    mPaint.textSize = scaleBoldSize
                    mPaint.typeface = Typeface.DEFAULT_BOLD
                }
                currentIndex - 1 -> {
                    mPaint.textSize = scoreSection.scoreTextSize.toFloat()
                    mPaint.typeface = Typeface.DEFAULT
                }
                else -> {
                    mPaint.textSize = scoreSection.scoreTextSize.toFloat()
                    mPaint.typeface = Typeface.DEFAULT
                }
            }
            val text = scoreSection.scoreText
            val x = scoreSection.atScorePercent.toFloat() / 100 * mWidth - mPaint.measureText(text) / 2
            val fontMetrics = mPaint.fontMetrics
            canvas.drawText(text, x, mHeight / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2, mPaint)
        }
    }

    /**
     * 每一段得分信息的封装
     */
    private data class ScoreSection(
        val scoreText: String, //对应分数段的文字
        val atScorePercent: Int, //对应分数段百分比取值  例如92%  这个值就传92
        var scoreTextSize: Int,
        var scoreBoldTextSize: Int,
        var scoreColor: Int
    )
}