package com.example.minedemo.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minedemo.R

class HandlerActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private val handlerPost = Handler()
    private var tvContent: TextView? = null
    private var btnSend: Button? = null
    private var btnSend2: Button? = null
    private var btnSend3: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_handler_activity)
        tvContent = findViewById(R.id.tv_content)
        btnSend = findViewById(R.id.btn_send)
        btnSend2 = findViewById(R.id.btn_send2)
        btnSend3 = findViewById(R.id.btn_send3)
        dealHandler()
        dealHandlerPost()
        dealHandlerPost2()
        dealHandlerPost3()
    }

    private fun dealHandlerPost3() {
        val threadPost3: Thread = object : Thread() {
            override fun run() {
                super.run()
                Looper.prepare()
                val handler2 = Handler()
                btnSend2!!.setOnClickListener {
                    Log.d(
                        TAG,
                        "当前的Thread---3--->$handler2"
                    )
                    handler2.postDelayed({
                        Log.d(
                            TAG,
                            "当前的Thread---3--->" + currentThread().name
                        )
                        Toast.makeText(this@HandlerActivity, "成功", Toast.LENGTH_SHORT)
                            .show()
                        tvContent!!.text = "子线程发送成功"
                    }, 3000)
                }
                Looper.loop()
            }
        }
        threadPost3.name = "three"
        threadPost3.start()
    }

    private fun dealHandlerPost2() {
        val threadPost1: Thread = object : Thread() {
            override fun run() {
                super.run()
                Looper.prepare()
                val handler2 = Handler()
                btnSend2!!.setOnClickListener {
                    Log.d(
                        TAG,
                        "当前的Thread---3--->$handler2"
                    )
                    handler2.post {
                        Log.d(
                            TAG,
                            "当前的Thread---3--->" + currentThread().name
                        )
                        Toast.makeText(this@HandlerActivity, "成功", Toast.LENGTH_SHORT)
                            .show()
                        tvContent!!.text = "子线程发送成功"
                    }
                }
                Looper.loop()
            }
        }
        threadPost1.name = "one"
        threadPost1.start()
    }

    private fun dealHandlerPost() {
        val threadPost: Thread = object : Thread() {
            override fun run() {
                super.run()
                btnSend!!.setOnClickListener {
                    handlerPost.post {
                        Log.d(
                            TAG,
                            "当前的Thread----2-->" + currentThread().name
                        )
                        tvContent!!.text = "发送消息成功"
                    }
                }
            }
        }
        threadPost.name = "post"
        threadPost.start()
    }

    private fun dealHandler() {
        val oneThread: Thread = object : Thread() {
            override fun run() {
                super.run()
                Looper.prepare()
                handler = object : Handler() {
                    override fun handleMessage(msg: Message) {
                        super.handleMessage(msg)
                        Log.d(
                            TAG,
                            "当前的Thread---1--->" + currentThread().name
                        )
                        tvContent!!.text = "子线程能更新吗？"
                    }
                }
                Looper.loop()
            }
        }
        val twoThread: Thread = object : Thread() {
            override fun run() {
                super.run()
                try {
                    sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                val message = handler!!.obtainMessage()
                message.obj = "two-send-one"
                handler!!.sendMessage(message)
            }
        }
        oneThread.name = "one-thread"
        oneThread.start()
        twoThread.name = "two-thread"
        twoThread.start()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
    }

    companion object {
        private const val TAG = "HandlerActivity"
    }
}