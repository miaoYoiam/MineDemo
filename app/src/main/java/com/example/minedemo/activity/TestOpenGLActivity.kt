package com.example.minedemo.activity

import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.opengl.GLES20
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BitmapCompat
import androidx.lifecycle.lifecycleScope
import com.example.minedemo.R
import com.example.minedemo.activity.opengl.GLSurface
import com.example.minedemo.activity.opengl.TestRenderer
import com.example.minedemo.utils.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10


class TestOpenGLActivity : AppCompatActivity() {
    private var glRenderer: TestRenderer? = null
    private var imageIv: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_open_glactivity)
        imageIv = findViewById<View>(R.id.iv_main_image) as ImageView
        glRenderer = TestRenderer()
//
//        val glPbufferSurface = GLSurface(256, 256)
//        glRenderer?.addSurface(glPbufferSurface)
//        glRenderer?.startRender()
//        glRenderer?.requestRender()
//        glRenderer!!.postRunnable {
//            val buffer: ByteBuffer = ByteBuffer.allocate(256 * 256 * 4)
//            buffer.order(ByteOrder.LITTLE_ENDIAN)
//            GLES20.glReadPixels(0, 0, 256, 256, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buffer)
//            buffer.rewind()
//            val bitmap: Bitmap? = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
//            bitmap?.copyPixelsFromBuffer(buffer)
//            Handler(Looper.getMainLooper()).post {
//                imageIv!!.setImageBitmap(bitmap)
//            }
//        }
        val sv = findViewById<View>(R.id.sv_main_demo) as SurfaceView
        sv.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                surfaceHolder.setFormat(PixelFormat.TRANSPARENT)

            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {
                val glWindowSurface = GLSurface(surfaceHolder.surface, width, height)
                glRenderer?.addSurface(glWindowSurface)
                glRenderer?.startRender()
                glRenderer?.requestRender()
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onDestroy() {
        super.onDestroy()
        glRenderer?.release()
        glRenderer = null
    }

    /**
     * 将数据转换成bitmap(OpenGL和Android的Bitmap色彩空间不一致，这里需要做转换)
     *
     * @param width 图像宽度
     * @param height 图像高度
     * @param ib 图像数据
     * @return bitmap
     */
    private fun frameToBitmap(width: Int, height: Int, ib: IntBuffer): Bitmap? {
        val pixs = ib.array()
        // 扫描转置(OpenGl:左上->右下 Bitmap:左下->右上)
        for (y in 0 until height / 2) {
            for (x in 0 until width) {
                val pos1 = y * width + x
                val pos2 = (height - 1 - y) * width + x
                val tmp = pixs[pos1]
                pixs[pos1] = pixs[pos2] and -0xff0100 or (pixs[pos2] shr 16 and 0xff) or (pixs[pos2] shl 16 and 0x00ff0000) // ABGR->ARGB
                pixs[pos2] = tmp and -0xff0100 or (tmp shr 16 and 0xff) or (tmp shl 16 and 0x00ff0000)
            }
        }
        if (height % 2 == 1) { // 中间一行
            for (x in 0 until width) {
                val pos = (height / 2 + 1) * width + x
                pixs[pos] = pixs[pos] and -0xff0100 or (pixs[pos] shr 16 and 0xff) or (pixs[pos] shl 16 and 0x00ff0000)
            }
        }
        return Bitmap.createBitmap(pixs, width, height, Bitmap.Config.ARGB_8888)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun readPixels(view: View) {
        val width = 200.dp.toInt()
        val height = 200.dp.toInt()

        lifecycleScope.launch {
            delay(1000)
            glRenderer?.postRunnable {
                val buffer: ByteBuffer = ByteBuffer.allocate(width * height * 2 * 2)
                GLES20.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buffer)
                val bitmap: Bitmap? = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap?.copyPixelsFromBuffer(buffer)
                Handler(Looper.getMainLooper()).post {
                    imageIv!!.setImageBitmap(bitmap)
                }
            }
        }
    }
}