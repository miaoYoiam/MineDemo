package com.example.minedemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.minedemo.R
import com.example.minedemo.asm.AsmRootDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)
    }

    fun startTrace(view: View) {
        AsmRootDelegate.startCollectMethodCost()
    }

    fun endTrace(view: View) {
        lifecycleScope.launch {
            val list = AsmRootDelegate.endCollectMethodCost()
            for (i in list.indices) {
                val item = list[i]
                Log.w("SimpleTrace", "Method:${item.name}  costTime:${item.costTime} ")
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RouterActivity.applicationContext, "耗时计算完成", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun jumpTestGlide(view: View) {

        val intent = Intent(this, TestGlideTransformActivity::class.java)
        startActivity(intent)
    }

    fun jumpTestViewPreview(view: View) {
        val intent = Intent(this, TestViewPreviewActivity::class.java)
        startActivity(intent)
    }

    fun jumpTestVolatile(view: View) {
        val intent = Intent(this, TestVolatileActivity::class.java)
        startActivity(intent)
    }

    fun jumpToViewDraw(view: View) {
        val intent = Intent(this, TestViewDraw0::class.java)
        startActivity(intent)
    }

    fun jumpToMediaMuxer(view: View) {
        val intent = Intent(this, TestMediaMuxerActivity::class.java)
        startActivity(intent)
    }

    fun jumpToOpenGl(view: View) {
        val intent = Intent(this, TestOpenGLActivity::class.java)
        startActivity(intent)
    }

}