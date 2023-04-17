package com.example.minedemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.lib_annotation.PagePath
import com.example.lib_annotation.PageType
import com.example.minedemo.R
import kotlinx.android.synthetic.main.activity_main.*

@PagePath(id = "main1", type = PageType.IActivity, allowMultiMode = false)
class MainActivity : AppCompatActivity() {
    var handler1: Handler? = null
    var runnable1: Runnable? = null
    var runnable2: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initThread()
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
    }

    private fun initThread() {
        runnable1 = Runnable {
            println(" runnable1===thread name==》${Thread.currentThread().name}")
            text.text = "runnable1"
            image.setImageResource(R.drawable.ic_launcher_foreground)
        }
        runnable2 = Runnable {
            println(" runnable2===thread name==》${Thread.currentThread().name}")
            text.text = "runnable2"
            image.setImageResource(R.drawable.ic_launcher_foreground)
        }
        val oneThread = object : Thread() {
            override fun run() {
                super.run()
                Looper.prepare()
                handler1 = Handler()
                handler1?.post(runnable1!!)
                Looper.loop()
            }
        }
        oneThread.name = "test1"
        oneThread.start()
    }

    fun handleMessage(view: View) {
        handler1?.post(runnable2!!)
    }
}