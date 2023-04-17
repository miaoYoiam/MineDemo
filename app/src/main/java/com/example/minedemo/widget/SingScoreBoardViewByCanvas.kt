package com.example.minedemo.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.example.minedemo.R
import com.example.minedemo.data.SongExData
import com.example.minedemo.utils.dp

/**
 * Day：2022/7/16 7:58 上午
 * @author zhanglei
 */
class SingScoreBoardViewByCanvas(private val context: Context) {
    private val exData = SongExData()

    private val vibratoLabel by lazy {
        context.resources.getString(R.string.Sing_Vibrato)
    }
    private var vibratoCountText = ""
    private var vibratoBitmap: Bitmap? = null
    private val vibratoRect by lazy {
        Rect()
    }

    private val glissandoLabel by lazy {
        context.resources.getString(R.string.Sing_Glissando)
    }
    private var glissanCountText = ""
    private var glissandoBitmap: Bitmap? = null
    private val glissandoRect by lazy {
        Rect()
    }

    private val mordentLabel by lazy {
        context.resources.getString(R.string.Sing_Mordent)
    }
    private var mordentCountText = ""
    private var mordentBitmap: Bitmap? = null
    private val mordentRect by lazy {
        Rect()
    }

    private val drawRect by lazy {
        Rect()
    }

    private val paint by lazy {
        Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
        }
    }

    private var fontMetrics: Paint.FontMetrics = Paint.FontMetrics()


    init {
        vibratoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.sing_vibrato)
        glissandoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.sing_portamento)
        mordentBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.sing_ornament)
    }

    private var mWidth = 0
    private var mHeight = 0

    fun setRect(width: Int, height: Int, top: Int, bottom: Int) {
        this.mWidth = width
        this.mHeight = height
        drawRect.top = top
        drawRect.left = 0
        drawRect.right = width
        drawRect.bottom = bottom
    }

    fun setSkillCount(data: SongExData?) {
        exData.glissandoTotal = data?.glissandoTotal ?: 0
        exData.mordentTotal = data?.mordentTotal ?: 0
        exData.vibratoTotal = data?.vibratoTotal ?: 0

        vibratoCountText = "0/${exData.vibratoTotal}"
        glissanCountText = "0/${exData.glissandoTotal}"
        mordentCountText = "0/${exData.mordentTotal}"
    }

    fun setSkillMatchCount(data: SongExData?) {
        exData.glissandoMatch = data?.glissandoMatch ?: 0
        exData.mordentMatch = data?.mordentMatch ?: 0
        exData.vibratoMatch = data?.vibratoMatch ?: 0

        vibratoCountText = "${exData.vibratoMatch}/${exData.vibratoTotal}"
        glissanCountText = "${exData.glissandoMatch}/${exData.glissandoTotal}"
        mordentCountText = "${exData.mordentMatch}/${exData.mordentTotal}"
    }

    fun onDraw(canvas: Canvas?) {
        canvas?.let { cs ->
            cs.clipRect(drawRect)
            val width = drawRect.width()
            var x = width.toFloat()
            val height = drawRect.height()
            val bitmapW = 15.dp.toInt()
            val bitmapH = 12.dp.toInt()

            paint.color = Color.BLACK
            canvas.drawRect(drawRect, paint)

            //mordent
            paint.color = Color.parseColor("#D98FFF")
            paint.textSize = 8.dp
            fontMetrics = paint.fontMetrics
            x -= paint.measureText(vibratoCountText)
            val baseLine0 = drawRect.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            cs.drawText(mordentCountText, x, baseLine0, paint)

            mordentBitmap?.let { bitmap ->
                x -= (bitmapW + 2.dp)
                mordentRect.set(x.toInt(), (height - bitmapH) / 2, (x + bitmapW).toInt(), (height - bitmapH) / 2 + bitmapH)
                cs.drawBitmap(bitmap, null, mordentRect, paint)
            }

            paint.color = Color.parseColor("#ffffff")
            paint.textSize = 14.dp
            fontMetrics = paint.fontMetrics
            x -= (paint.measureText(mordentLabel) + 2.dp)
            val baseLine1 = drawRect.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            cs.drawText(mordentLabel, x, baseLine1, paint)


            //glissando
            paint.color = Color.parseColor("#FFD016")
            paint.textSize = 8.dp
            fontMetrics = paint.fontMetrics
            x -= (paint.measureText(glissanCountText) + 18.dp)
            val baseLine2 = drawRect.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            cs.drawText(glissanCountText, x, baseLine0, paint)

            glissandoBitmap?.let { bitmap ->
                x -= (bitmapW + 2.dp)
                glissandoRect.set(x.toInt(), (height - bitmapH) / 2, (x + bitmapW).toInt(), (height - bitmapH) / 2 + bitmapH)
                cs.drawBitmap(bitmap, null, glissandoRect, paint)
            }

            paint.color = Color.parseColor("#ffffff")
            paint.textSize = 14.dp
            fontMetrics = paint.fontMetrics
            x -= (paint.measureText(glissandoLabel) + 2.dp)
            val baseLine3 = drawRect.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            cs.drawText(glissandoLabel, x, baseLine1, paint)


            //vibrato
            paint.color = Color.parseColor("#00C6FF")
            paint.textSize = 8.dp
            fontMetrics = paint.fontMetrics
            x -= (paint.measureText(vibratoCountText) + 18.dp)
            val baseLine4 = drawRect.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            cs.drawText(vibratoCountText, x, baseLine0, paint)

            vibratoBitmap?.let { bitmap ->
                x -= (bitmapW + 2.dp)
                vibratoRect.set(x.toInt(), (height - bitmapH) / 2, (x + bitmapW).toInt(), (height - bitmapH) / 2 + bitmapH)
                cs.drawBitmap(bitmap, null, vibratoRect, paint)
            }

            paint.color = Color.parseColor("#ffffff")
            paint.textSize = 14.dp
            fontMetrics = paint.fontMetrics
            x -= (paint.measureText(vibratoLabel) + 2.dp)
            val baseLine5 = drawRect.centerY() + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            cs.drawText(vibratoLabel, x, baseLine1, paint)


        }
    }

    private fun SongExData?.valid(): Boolean {
        this ?: return false
        return !(this.glissandoTotal == 0 && this.mordentTotal == 0 && this.vibratoTotal == 0)
    }
}